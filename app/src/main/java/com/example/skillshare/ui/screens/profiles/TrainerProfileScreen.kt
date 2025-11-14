package com.example.skillshare.ui.screens.profiles

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.skillshare.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid ?: ""
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance().reference

    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(auth.currentUser?.email ?: "") }
    var photoUrl by remember { mutableStateOf<String?>(null) }
    var status by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    // Image picker launcher
    var pickedUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        pickedUri = uri
    }


    LaunchedEffect(uid) {
        if (uid.isNotBlank()) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    name = doc.getString("username") ?: ""
                    bio = doc.getString("bio") ?: ""
                    photoUrl = doc.getString("photoUrl")
                }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("My Profile", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            ) {
                val painter = rememberAsyncImagePainter(model = pickedUri ?: photoUrl)
                if (photoUrl != null) {
                    Image(
                        painter = painter,
                        contentDescription = "Profile photo",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                } else {

                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(name.takeIf { it.isNotBlank() }?.firstOrNull()?.toString() ?: "U")
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(onClick = { launcher.launch("image/*") }) {
                Text("Upload Photo")
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Bio") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                // Save profile (and optionally upload image first)
                loading = true
                status = "Saving..."
                val onSaveComplete: (String) -> Unit = { msg ->
                    loading = false
                    status = msg
                    // Navigate back to the dashboard on success
                    if (msg.contains("successfully")) {
                        navController.navigate(Screen.TrainerDashboard.route) {
                            popUpTo(Screen.TrainerDashboard.route) { inclusive = true }
                        }
                    }
                }
                if (pickedUri != null) {
                    val uri = pickedUri!!
                    val ref = storage.child("profile_photos/$uid.jpg")
                    ref.putFile(uri)
                        .addOnSuccessListener {
                            ref.downloadUrl.addOnSuccessListener { downloadUrl ->
                                saveProfile(db, uid, name, bio, downloadUrl.toString()) {
                                    loading = false
                                    status = it
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            loading = false
                            status = "Upload failed: ${e.message}"
                        }
                } else {
                    saveProfile(db, uid, name, bio, photoUrl) { msg ->
                        loading = false
                        status = msg
                    }
                }
            }, enabled = !loading, modifier = Modifier.fillMaxWidth()) {
                Text("Save Changes")
            }

            Spacer(Modifier.height(12.dp))

            Text(status)
        }
    }
}

private fun saveProfile(db: FirebaseFirestore, uid: String, name: String, bio: String, photoUrl: String?, onDone: (String) -> Unit) {
    val data = hashMapOf<String, Any>(
        "name" to name,
        "bio" to bio
    )
    photoUrl?.let { data["photoUrl"] = it }
    db.collection("users").document(uid)
        .set(data, com.google.firebase.firestore.SetOptions.merge())
        .addOnSuccessListener { onDone("Profile saved") }
        .addOnFailureListener { e -> onDone("Error: ${e.message}") }
}
