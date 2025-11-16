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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillshare.viewmodel.SkillListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillDetailScreen(
    navController: NavController,
    skillId: String,
    viewModel: SkillListViewModel
) {
    LaunchedEffect(skillId) {
        viewModel.loadSkillById(skillId)
    }

    val selectedSkill by viewModel.selectedSkill.collectAsState()

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
            selectedSkill?.let { skill ->
                Text(
                    text = skill.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = skill.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Cost: KSH ${skill.cost}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Duration: ${skill.duration} minutes",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Location: ${skill.location}",
                    style = MaterialTheme.typography.bodyMedium
                )
                // TODO: Add video player if skill.videoUrl is not null
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { /* TODO: Implement order logic */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Order")
                }
            } ?: run {
                Text("Loading skill details...")
            }
        }
    }
}
