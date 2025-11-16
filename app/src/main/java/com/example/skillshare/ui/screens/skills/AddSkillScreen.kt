package com.example.skillshare.ui.screens.skills

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.skillshare.viewmodel.SkillListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Helper function to get a displayable name from a content URI
private fun getFileName(context: Context, uri: Uri): String {
    context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0) {
                return cursor.getString(nameIndex)
            }
        }
    }
    // Fallback if the query fails or the column doesn't exist
    return uri.lastPathSegment?.substringAfterLast("/") ?: "Unknown file"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSkillScreen(
    viewModel: SkillListViewModel,
    onBack: () -> Unit,
    onSkillAdded: () -> Unit,
    trainerId: String
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedVideoName by remember { mutableStateOf("No video selected") }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedVideoUri = uri
        selectedVideoName = uri?.let { getFileName(context, it) } ?: "No video selected"
    }

    LaunchedEffect(Unit) {
        viewModel.addSkillResult.collectLatest { result ->
            result.fold(
                onSuccess = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Skill added successfully!")
                        onSkillAdded()
                    }
                },
                onFailure = { exception ->
                    scope.launch {
                        snackbarHostState.showSnackbar(exception.message ?: "An unknown error occurred")
                    }
                }
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add New Skill") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Skill Name") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = isLoading
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                readOnly = isLoading
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (in minutes)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                readOnly = isLoading
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cost,
                onValueChange = { cost = it },
                label = { Text("Cost") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                readOnly = isLoading
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { videoPickerLauncher.launch("video/*") }, enabled = !isLoading) {
                    Icon(Icons.Default.Videocam, contentDescription = "Select Video")
                    Spacer(Modifier.width(8.dp))
                    Text("Select Video")
                }
                Spacer(Modifier.width(16.dp))
                Text(
                    text = selectedVideoName,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val durationInt = duration.toIntOrNull()
                    val costDouble = cost.toDoubleOrNull()

                    if (title.isBlank() || description.isBlank() || duration.isBlank() || cost.isBlank() || location.isBlank()) {
                        scope.launch { snackbarHostState.showSnackbar("Please fill all fields.") }
                        return@Button
                    }

                    if (durationInt == null) {
                        scope.launch { snackbarHostState.showSnackbar("Please enter a valid number for duration.") }
                        return@Button
                    }

                    if (costDouble == null) {
                        scope.launch { snackbarHostState.showSnackbar("Please enter a valid number for cost.") }
                        return@Button
                    }

                    if (trainerId.isBlank()) {
                        scope.launch { snackbarHostState.showSnackbar("Error: Could not verify user. Please log in again.") }
                        return@Button
                    }

                    if (selectedVideoUri == null) {
                        scope.launch { snackbarHostState.showSnackbar("Please select a video to upload.") }
                        return@Button
                    }

                    viewModel.addSkill(
                        title = title,
                        description = description,
                        duration = durationInt,
                        cost = costDouble,
                        location = location,
                        trainerId = trainerId,
                        videoUri = selectedVideoUri
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Skill")
                }
            }
        }
    }
}
