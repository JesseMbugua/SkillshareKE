package com.example.skillshare.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillshare.navigation.Screen

@Composable
fun SearchScreen(navController: NavController) {
    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Search skills or trainers") })
        Spacer(Modifier.height(12.dp))
        Button(onClick = { navController.navigate(Screen.Details.route) }) {
            Text("View Trainer Details")
        }
    }
}
