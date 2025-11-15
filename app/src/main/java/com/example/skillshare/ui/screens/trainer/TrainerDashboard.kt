package com.example.skillshare.ui.screens.trainer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.skillshare.model.Skill
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun TrainerDashboard(
    onAddSkill: () -> Unit,
    onSkillClick: (Skill) -> Unit
) {
    var skills by remember { mutableStateOf<List<Skill>>(emptyList()) }
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    LaunchedEffect(key1 = currentUser) {
        if (currentUser != null) {
            // Fetch skills created by the current trainer
            firestore.collection("skills")
                .whereEqualTo("trainerId", currentUser.uid)
                .get()
                .addOnSuccessListener { documents ->
                    val skillList = documents.toObjects(Skill::class.java)
                    skills = skillList
                }
                .addOnFailureListener { exception ->
                    // Handle error
                }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddSkill) {
                Text("+")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "My Skills",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(skills) { skill ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        onClick = { onSkillClick(skill) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = skill.title, style = MaterialTheme.typography.bodyLarge)
                            Text(text = skill.location, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
