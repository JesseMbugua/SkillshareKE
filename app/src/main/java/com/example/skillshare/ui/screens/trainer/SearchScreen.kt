package com.example.skillshare.ui.screens.trainer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.skillshare.ui.screens.skills.SkillListScreen
import com.example.skillshare.viewmodel.SkillListViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SkillListViewModel,
    onSkillClick: (String) -> Unit
) {
    // When the SearchScreen is displayed, we ensure that any trainer-specific
    // filter is removed. This guarantees the user always sees the full list of skills.
    LaunchedEffect(Unit) {
        viewModel.loadSkills()
    }

    // The SkillListScreen contains the UI for filtering and displaying skills.
    // It is used by both trainers and users. We pass the ViewModel and click
    // handler down to it.
    SkillListScreen(
        onSkillClick = onSkillClick,
        viewModel = viewModel
    )
}
