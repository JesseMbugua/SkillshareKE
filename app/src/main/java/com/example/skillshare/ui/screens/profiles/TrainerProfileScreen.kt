package com.example.skillshare.ui.screens.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TrainerProfileScreen(navController: NavController) {
    Column(Modifier.padding(16.dp)) {
        Text("Trainer Profile", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Name: Jane Doe")
        Text("Skill: Digital Art")
        Text("Experience: 5 years")
        Text("Rate: KES 1500/hr")

        Spacer(Modifier.height(12.dp))
        Button(onClick = { navController.navigate("booking") }) {
            Text("Book Trainer")
        }
    }
}
