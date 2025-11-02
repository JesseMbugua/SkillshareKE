package com.example.skillshare.ui.screens.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillshare.navigation.Screen

@Composable
fun BookingScreen(navController: NavController) {
    Column(Modifier.padding(16.dp)) {
        Text("Book a Session", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Preferred Date & Time") })
        Spacer(Modifier.height(12.dp))
        Button(onClick = { navController.navigate(Screen.MyBookings.route) }) {
            Text("Confirm Booking")
        }
    }
}
