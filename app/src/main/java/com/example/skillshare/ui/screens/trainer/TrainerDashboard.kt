package com.example.skillshare.ui.screens.trainer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.example.skillshare.navigation.Screen

import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun TrainerDashboardScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    var trainerName by remember { mutableStateOf("") }
    var trainerEmail by remember { mutableStateOf("") }
    var trainerCounty by remember { mutableStateOf("") }

    var skills by remember { mutableStateOf(listOf<Map<String, String>>()) }
    var bookings by remember { mutableStateOf(listOf<Map<String, String>>()) }

    // Fetch trainer info and data
    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { doc ->
                    trainerName = doc.getString("name") ?: ""
                    trainerEmail = doc.getString("email") ?: ""
                    trainerCounty = doc.getString("county") ?: ""
                }

            db.collection("skills")
                .whereEqualTo("trainerId", userId)
                .get()
                .addOnSuccessListener { result ->
                    skills = result.documents.map { it.data as Map<String, String> }
                }

            db.collection("bookings")
                .whereEqualTo("trainerId", userId)
                .get()
                .addOnSuccessListener { result ->
                    bookings = result.documents.map { it.data as Map<String, String> }
                }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Trainer header info
            Text(
                text = "Welcome, $trainerName",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Email: $trainerEmail")
            Text(text = "County: $trainerCounty")

            Divider()

            // Skills section
            Text(
                text = "My Skill Listings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (skills.isEmpty()) {
                Text("You haven't added any skills yet.")
            } else {
                LazyColumn {
                    items(skills) { skill ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = skill["title"] ?: "Untitled", fontWeight = FontWeight.Bold)
                                Text(text = skill["description"] ?: "")
                                Spacer(Modifier.height(8.dp))
                                Button(onClick = { /* TODO: Edit skill */ }) {
                                    Text("Edit")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { navController.navigate(Screen.AddSkill.route) },
                modifier = Modifier.fillMaxWidth()

            ) {
                Text("Add New Skill")
            }

            Divider()

            // Bookings section
            Text(
                text = "Incoming Bookings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (bookings.isEmpty()) {
                Text("No new bookings yet.")
            } else {
                LazyColumn {
                    items(bookings) { booking ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Booked by: ${booking["learnerName"] ?: "Unknown"}",
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Skill: ${booking["skillName"] ?: "N/A"}")
                                Text("Status: ${booking["status"] ?: "Pending"}")
                                Spacer(Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(onClick = { /* TODO: Accept */ }) { Text("Accept") }
                                    OutlinedButton(onClick = { /* TODO: Decline */ }) { Text("Decline") }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.TrainerDashboard.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text("Log Out")
            }
        }
    }
}