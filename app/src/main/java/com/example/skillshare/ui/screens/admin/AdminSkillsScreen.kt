package com.example.skillshare.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

data class AdminSkill(
    val id: String,
    val title: String,
    val trainerId: String,
    val trainerEmail: String?,
    val price: Double,
    val videoUrl: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSkillsScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()

    var skills by remember { mutableStateOf<List<AdminSkill>>(emptyList()) }

    // Load skills from Firestore live
    LaunchedEffect(true) {
        db.collection("skills")
            .addSnapshotListener { snapshot, _ ->
                skills = snapshot?.documents?.map { doc ->
                    AdminSkill(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        trainerId = doc.getString("trainerId") ?: "",
                        trainerEmail = doc.getString("trainerEmail"),
                        price = doc.getDouble("cost") ?: 0.0,
                        videoUrl = doc.getString("videoUrl")
                    )
                } ?: emptyList()
            }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Manage Skills") }) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Text(
                "All Skills",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(skills) { skill ->
                    AdminSkillRow(
                        skill = skill,
                        onDelete = {
                            // delete Firestore doc
                            db.collection("skills").document(skill.id).delete()

                            // if you want, delete video from R2 (optional)
                            // Call your Node backend delete endpoint here
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminSkillRow(skill: AdminSkill, onDelete: () -> Unit) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(12.dp)) {

            Text(skill.title, fontWeight = FontWeight.Bold)
            Text("Trainer: ${skill.trainerEmail ?: skill.trainerId}")
            Text("Price: Ksh ${skill.price.toInt()}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween) {

                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete skill")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Take Down")
                }
            }
        }
    }
}
