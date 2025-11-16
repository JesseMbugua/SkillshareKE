package com.example.skillshare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skillshare.data.Skill // Correct Data Model
import com.example.skillshare.viewmodel.SkillListViewModel

// Renamed to TrainerSkillsScreen for consistency with NavGraph
// Accepts lambdas for navigation, not the NavController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerSkillsScreen(
    onSkillClick: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SkillListViewModel = viewModel() // Simplified ViewModel creation
) {
    val skills by viewModel.skills.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Skills") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                SkillList(
                    skills = skills,
                    onSkillClick = onSkillClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun SkillList(
    skills: List<Skill>,
    onSkillClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(skills) { skill ->
            SkillCard(skill = skill, onSkillClick = { onSkillClick(skill.id) })
        }
    }
}

@Composable
fun SkillCard(
    skill: Skill,
    onSkillClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onSkillClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = skill.title, style = MaterialTheme.typography.titleMedium)
            Text(text = skill.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SkillListPreview() {
    val dummySkills = listOf(
        Skill(id = "1", title = "Kotlin for Beginners", description = "Learn the basics of Kotlin", duration = 60, cost = 25.0, location = "Online"),
        Skill(id = "2", title = "Advanced Android", description = "Deep dive into Android development", duration = 120, cost = 75.0, location = "Online")
    )
    SkillList(skills = dummySkills, onSkillClick = {})
}
