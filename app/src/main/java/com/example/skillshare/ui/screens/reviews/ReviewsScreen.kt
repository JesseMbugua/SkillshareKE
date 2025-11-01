package com.example.skillshare.ui.screens.reviews

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReviewScreen(navController: androidx.navigation.NavController) {
    Column(Modifier.padding(16.dp)) {
        Text("Leave a Review", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Write your review...") })
        Spacer(Modifier.height(12.dp))
        Button(onClick = { /* future submit logic */ }) {
            Text("Submit Review")
        }
    }
}
