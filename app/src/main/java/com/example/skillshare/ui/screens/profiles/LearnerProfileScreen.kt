package com.example.skillshare.ui.screens.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LearnerProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("john@example.com") }
    var location by remember { mutableStateOf("Nairobi") }
    var isEditing by remember { mutableStateOf(false) }

    Column(Modifier.padding(16.dp)) {
        Text("My Profile", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { if (isEditing) name = it },
            label = { Text("Full Name") },
            enabled = isEditing
        )
        OutlinedTextField(
            value = email,
            onValueChange = { if (isEditing) email = it },
            label = { Text("Email") },
            enabled = isEditing
        )
        OutlinedTextField(
            value = location,
            onValueChange = { if (isEditing) location = it },
            label = { Text("Location") },
            enabled = isEditing
        )

        Spacer(Modifier.height(12.dp))

        Button(onClick = { isEditing = !isEditing }) {
            Text(if (isEditing) "Save Changes" else "Edit Profile")
        }
    }
}
