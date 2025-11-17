package com.example.skillshare.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.skillshare.navigation.Screen

@Composable
fun UserDashboardScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var county by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { doc ->
                    name = doc.getString("displayName") ?: ""
                    //email = doc.getString("email") ?: ""
                   // county = doc.getString("county") ?: ""
                }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome, $name!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            //Text("Email: $email")
           // Text("County: $county")
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate(Screen.LearnerProfile.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View/Edit My Profile")
            }

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.UserDashboard.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text("Log Out")
            }
        }
    }
}