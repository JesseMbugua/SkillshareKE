package com.example.skillshare.ui.screens.payment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillshare.navigation.Screen

@Composable
fun PaymentScreen(navController: NavController) {
    var method by remember { mutableStateOf("M-Pesa") }
    var amount by remember { mutableStateOf("1500") }

    Column(Modifier.padding(16.dp)) {
        Text("Payment", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = method, onValueChange = { method = it }, label = { Text("Payment Method") })
        OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount (KES)") })
        Spacer(Modifier.height(12.dp))
        Button(onClick = { /* No-op */ }) {
            Text("Confirm Payment")
        }
    }
}
