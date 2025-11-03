package com.example.skillshare.ui.screens.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TrainerProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("Jane Doe") }
    var skill by remember { mutableStateOf("Painting") }
    var price by remember { mutableStateOf("1500") }

    Column(Modifier.padding(16.dp)) {
        Text("Trainer Profile", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") })
        OutlinedTextField(value = skill, onValueChange = { skill = it }, label = { Text("Skill Taught") })
        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Rate (KES/hr)") })
        Spacer(Modifier.height(12.dp))
        Button(onClick = { /* Save changes */ }) { Text("Update Profile") }
    }
}
