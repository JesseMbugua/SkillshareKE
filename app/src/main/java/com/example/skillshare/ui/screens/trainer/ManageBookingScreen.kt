package com.example.skillshare.ui.screens.trainer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ManageBookingsScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid

    var bookings by remember { mutableStateOf(listOf<Map<String, Any>>()) }

    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("bookings").whereEqualTo("trainerId", uid)
                .addSnapshotListener { snap, _ ->
                    bookings = snap?.documents?.mapNotNull { it.data?.plus("id" to it.id) } ?: emptyList()
                }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Incoming Bookings", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))

            if (bookings.isEmpty()) {
                Text("No bookings")
            } else {
                LazyColumn {
                    items(bookings) { b ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Learner: ${b["learnerName"] ?: "Unknown"}", style = MaterialTheme.typography.titleMedium)
                                Text("Skill: ${b["skillName"] ?: "N/A"}")
                                Text("Status: ${b["status"] ?: "Pending"}")
                                Spacer(Modifier.height(8.dp))
                                Row {
                                    Button(onClick = {
                                        val id = b["id"] as? String ?: return@Button
                                        db.collection("bookings").document(id).update("status", "Accepted")
                                    }) { Text("Accept") }
                                    Spacer(Modifier.width(8.dp))
                                    OutlinedButton(onClick = {
                                        val id = b["id"] as? String ?: return@OutlinedButton
                                        db.collection("bookings").document(id).update("status", "Declined")
                                    }) { Text("Decline") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
