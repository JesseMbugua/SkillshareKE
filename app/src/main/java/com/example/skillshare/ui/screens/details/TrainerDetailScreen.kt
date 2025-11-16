package com.example.skillshare.ui.screens.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.skillshare.viewmodel.SkillListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerDetailScreen(
    navController: NavController,
    skillId: String?,
    viewModel: SkillListViewModel = viewModel()
) {
    // Find the skill by its ID from the shared ViewModel
    val skill by viewModel.skills.collectAsState(initial = emptyList())
    val selectedSkill = skill.find { it.id == skillId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedSkill?.title ?: "Skill Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            if (selectedSkill != null) {
                Text(
                    text = selectedSkill.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = selectedSkill.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Cost: KSH ${selectedSkill.cost}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Duration: ${selectedSkill.duration} minutes",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Location: ${selectedSkill.location}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { /* TODO: Implement order logic */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Order")
                }
            } else {
                Text("Skill not found.")
            }
        }
    }
}
