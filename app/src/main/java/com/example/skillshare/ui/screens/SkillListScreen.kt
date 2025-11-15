package com.example.skillshare.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.skillshare.model.Skill
import com.example.skillshare.network.RetrofitInstance
import com.example.skillshare.viewmodel.SkillListViewModel

// This composable connects to the ViewModel (Stateful)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,

    skillListViewModel: SkillListViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SkillListViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SkillListViewModel(RetrofitInstance.api) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )
) {
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
        },
        modifier = modifier
    ) { innerPadding ->
        // It calls the stateless composable with the current state
        SkillList(
            skills = skills,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}


@Composable
fun SkillList(skills: List<Skill>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(skills) { skill ->

            Text(text = skill.title)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SkillListScreenPreview() {
    // Preview with dummy data
    val dummySkills = listOf(
        Skill(id = "1", title = "Kotlin for Beginners", description = "Learn the basics of Kotlin", duration = 60, cost = 25.0, location = "Online"),
        Skill(id = "2", title = "Advanced Android", description = "Deep dive into Android development", duration = 120, cost = 75.0, location = "Online")
    )


    SkillList(skills = dummySkills)
}
