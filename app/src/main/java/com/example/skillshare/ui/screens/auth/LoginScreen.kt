package com.example.skillshare.ui.screens.auth

import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillshare.navigation.Screen

@Composable
fun LoginScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome Back 👋", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        status = "Please fill all fields"
                    } else {
                        isLoading = true
                        db.collection("users")
                            .whereEqualTo("email", email.lowercase())
                            .whereEqualTo("password", password)
                            .get()
                            .addOnSuccessListener { documents ->
                                isLoading = false
                                if (!documents.isEmpty) {
                                    val userDoc = documents.documents[0]
                                    val role = userDoc.getString("role") ?: "user"

                                    status = "Login successful!"

                                    if (role == "trainer") {
                                        navController.navigate(Screen.TrainerDashboard.route) {
                                            popUpTo(Screen.Login.route) { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate(Screen.UserDashboard.route) {
                                            popUpTo(Screen.Login.route) { inclusive = true }
                                        }
                                    }
                                } else {
                                    status = "Invalid credentials"
                                }
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                status = "Error: ${e.message}"
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Login")
                }
            }
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = { navController.navigate(Screen.Signup.route) }) {
                Text("Don’t have an account? Sign up")
            }

            Spacer(Modifier.height(16.dp))
            Text(status)
        }
    }
}
