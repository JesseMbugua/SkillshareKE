package com.example.skillshare.ui.screens.mybookings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyBookingsScreen(navController: androidx.navigation.NavController) {
    Column(Modifier.padding(16.dp)) {
        Text("My Bookings", style = MaterialTheme.typography.titleLarge)
        Text("• Art Lesson with Jane Doe - Confirmed")
        Text("• Photography with Alex Kim - Pending")
    }
}
