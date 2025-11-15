package com.example.skillshare.ui.screens.skills

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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
import com.example.skillshare.model.Skill
import com.example.skillshare.network.RetrofitInstance
import com.example.skillshare.viewmodel.SkillListViewModel
import com.example.skillshare.viewmodel.SkillListViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillListScreen(navController: NavController) {
    val skillListViewModel: SkillListViewModel = viewModel(
        factory = SkillListViewModelFactory(RetrofitInstance.api)
    )
    val skills by skillListViewModel.skills.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trainer Skill Listings") },
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
    ) { paddingValues ->
        SkillList(
            skills = skills,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun SkillList(skills: List<Skill>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(skills) { skill ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = skill.title, style = MaterialTheme.typography.bodyLarge)
                    Text(text = skill.location, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
