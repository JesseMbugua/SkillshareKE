package com.example.skillshare.ui.screens.trainer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.skillshare.model.Skill
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerDashboard(
    onViewSkills: () -> Unit,
    onAddSkill: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trainer Dashboard") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onViewSkills) {
                Text("View My Skills")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onAddSkill) {
                Text("Add a New Skill")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerSkillsScreen(
    onSkillClick: (Skill) -> Unit,
    onBack: () -> Unit
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
        topBar = {
            TopAppBar(
                title = { Text("My Skills") },
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
                .padding(16.dp)
        ) {
            if (skills.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("You have not added any skills yet.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(skills) { skill ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
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
}
