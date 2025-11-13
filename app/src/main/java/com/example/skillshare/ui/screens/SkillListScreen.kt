package com.example.skillshare.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skillshare.model.Skill
import com.example.skillshare.viewmodel.SkillListViewModel

// You won't need these for the preview itself anymore
// import com.example.skillshare.network.RetrofitInstance
// import com.example.skillshare.viewmodel.SkillListViewModel

// This composable connects to the ViewModel (Stateful)
@Composable
fun SkillListScreen(
    modifier: Modifier = Modifier,
    skillListViewModel: SkillListViewModel = viewModel()
) {
    val skills by skillListViewModel.skills.collectAsState()
    // It calls the stateless composable with the current state
    SkillList(skills = skills, modifier = modifier)
}

// This is your new stateless composable for the UI
@Composable
fun SkillList(skills: List<Skill>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(skills) { skill ->
            // Consider creating a dedicated SkillItem composable for better structure
            Text(text = skill.title)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SkillListScreenPreview() {
    // Preview with dummy data
    val dummySkills = listOf(
        Skill("1", "Kotlin for Beginners", "Learn the basics of Kotlin", 60, 25.0, "Online"),
        Skill("2", "Advanced Android", "Deep dive into Android development", 120, 75.0, "Online")
    )

    // Now, you can directly preview the stateless SkillList composable
    SkillList(skills = dummySkills)
}
