package com.example.skillshare.ui.screens.trainer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillshare.ui.screens.skills.SkillListScreen
import com.example.skillshare.viewmodel.SkillListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SkillListViewModel,
    onSkillClick: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid

    var displayName by remember { mutableStateOf("") }

    // Load skills when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadSkills()
    }

    // Load user display name
    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { doc ->
                    displayName = doc.getString("displayName") ?: ""
                }
        }
    }

    Column {
        Text(
            text = "Welcome, $displayName",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        SkillListScreen(
            onSkillClick = onSkillClick,
            viewModel = viewModel
        )
    }
}
