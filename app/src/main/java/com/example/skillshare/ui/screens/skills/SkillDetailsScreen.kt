package com.example.skillshare.ui.screens.skills

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skillshare.ui.composables.VideoPlayer
import com.example.skillshare.viewmodel.SkillListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillDetailsScreen(
    skillId: String,
    viewModel: SkillListViewModel,
    onBack: () -> Unit
) {
    // Observe the selected skill from the ViewModel
    val skill by viewModel.selectedSkill.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Load the skill details when the screen is first composed
    LaunchedEffect(skillId) {
        viewModel.loadSkillById(skillId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(skill?.title ?: "Skill Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (isLoading && skill == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (skill != null) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Video Player
                    if (!skill!!.videoUrl.isNullOrBlank()) {
                        VideoPlayer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f),
                            videoUrl = skill!!.videoUrl!!
                        )
                    } else {
                        // Placeholder for when there's no video
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .background(Color.Gray.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No video available", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(skill!!.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(skill!!.description, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Ksh ${skill!!.cost}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else if (!isLoading) {
                Text(
                    text = "Skill not found.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
