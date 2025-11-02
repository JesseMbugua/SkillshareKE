package com.example.skillshare.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillshare.navigation.Screen

@Composable
fun TrainerDetailScreen(navController: NavController) {
    Column(Modifier.padding(16.dp)) {
        Text("Trainer: Jane Doe", style = MaterialTheme.typography.titleLarge)
        Text("Skill: Painting - KES 1500/hr")
        Spacer(Modifier.height(12.dp))
        Button(onClick = { navController.navigate(Screen.Booking.route) }) {
            Text("Book Session")
        }
    }
}
