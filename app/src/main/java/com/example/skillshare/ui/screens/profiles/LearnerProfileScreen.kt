package com.example.skillshare.ui.screens.profiles

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.skillshare.R
import com.example.skillshare.model.ProfileRepository
import com.example.skillshare.model.ProfileSessionManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.navigation.NavController
import com.example.skillshare.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnerProfileScreen(navController: NavController) {

    val repo = remember { ProfileRepository() }
    val profile by ProfileSessionManager.currentProfile.collectAsState()

    // Editable fields
    var username by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    var pickedUri by remember { mutableStateOf<Uri?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        pickedUri = it
    }

    // Load saved profile data
    LaunchedEffect(profile) {
        username = profile?.username ?: ""
        fullName = profile?.displayName ?: ""
        bio = profile?.bio ?: ""
    }

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("My Profile", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        // Profile picture + edit controls
        Row(verticalAlignment = Alignment.CenterVertically) {

            val imagePainter = rememberAsyncImagePainter(
                model = pickedUri ?: profile?.photoUrl,
                placeholder = painterResource(R.drawable.default_pfp),
                error = painterResource(R.drawable.default_pfp)
            )

            Image(
                painter = imagePainter,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.width(16.dp))

            Column {

                IconButton(onClick = { launcher.launch("image/*") }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Photo")
                }

                if (profile?.photoUrl != null || pickedUri != null) {
                    IconButton(
                        onClick = {
                            pickedUri = null
                            CoroutineScope(Dispatchers.IO).launch {
                                repo.deleteOldProfilePicture()
                                repo.saveProfile(
                                    username = username,
                                    displayName = fullName,
                                    bio = bio,
                                    photoUrl = null
                                )
                            }
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Photo")
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // EDIT toggle
        Button(
            onClick = { isEditing = !isEditing },
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (isEditing) "Cancel Editing" else "Edit Profile") }

        Spacer(Modifier.height(20.dp))

        // USERNAME
        OutlinedTextField(
            value = username,
            onValueChange = { if (isEditing) username = it },
            label = { Text("Username") },
            enabled = isEditing,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Choose a username") }
        )

        Spacer(Modifier.height(12.dp))

        // FULL NAME
        OutlinedTextField(
            value = fullName,
            onValueChange = { if (isEditing) fullName = it },
            label = { Text("Full Name") },
            enabled = isEditing,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Optional full name") }
        )

        Spacer(Modifier.height(12.dp))

        // BIO
        OutlinedTextField(
            value = bio,
            onValueChange = { if (isEditing) bio = it },
            label = { Text("Bio") },
            enabled = isEditing,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Tell people about yourself") }
        )

        Spacer(Modifier.height(20.dp))

        // SAVE CHANGES BUTTON
        if (isEditing) {
            Button(
                onClick = {
                    isSaving = true
                    status = "Saving..."

                    CoroutineScope(Dispatchers.IO).launch {

                        try {
                            var newPhotoUrl = profile?.photoUrl

                            if (pickedUri != null) {
                                newPhotoUrl = repo.uploadProfileImage(pickedUri!!)
                            }

                            repo.saveProfile(
                                username = username.ifEmpty { profile?.username ?: "" },
                                displayName = fullName,
                                bio = bio,
                                photoUrl = newPhotoUrl
                            )

                            withContext(Dispatchers.Main) {
                                isSaving = false
                                status = "Saved!"
                                isEditing = false
                            }

                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                isSaving = false
                                status = "Error: ${e.message}"
                            }
                        }
                    }
                },
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(status)

        Spacer(Modifier.height(30.dp))

        // LOGOUT
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                ProfileSessionManager.clearSession()
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}
