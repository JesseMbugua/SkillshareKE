package com.example.skillshare.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillshare.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LoginScreen(navController: NavController) {

    val auth = FirebaseAuth.getInstance()
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

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        status = "❌ Please fill all fields"
                        return@Button
                    }

                    isLoading = true

                    auth.signInWithEmailAndPassword(email.trim(), password)
                        .addOnSuccessListener { result ->
                            val uid = result.user!!.uid

                            db.collection("users").document(uid).get()
                                .addOnSuccessListener { doc ->
                                    isLoading = false

                                    if (!doc.exists()) {
                                        status = "❌ User data missing"
                                        return@addOnSuccessListener
                                    }

                                    val role = doc.getString("role") ?: "user"

// Check banned status
                                    val banned = when (val b = doc.get("banned")) {
                                        is Boolean -> b
                                        is String -> b.equals("true", ignoreCase = true)
                                        is Number -> b.toInt() == 1
                                        else -> false
                                    }
                                    if (banned) {
                                        status = "❌ This account has been banned"
                                        return@addOnSuccessListener
                                    }


// Redirect based on role
                                    status = "✅ Login successful!"

                                    when (role.lowercase()) {
                                        "admin" -> {
                                            navController.navigate(Screen.AdminDashboard.route) {
                                                popUpTo(Screen.Login.route) { inclusive = true }
                                            }
                                        }

                                        "trainer" -> {
                                            navController.navigate(Screen.TrainerDashboard.createRoute(uid)) {
                                                popUpTo(Screen.Login.route) { inclusive = true }
                                            }
                                        }

                                        else -> { // normal user
                                            navController.navigate(Screen.LearnerProfile.route) {
                                                popUpTo(Screen.Login.route) { inclusive = true }
                                            }
                                        }
                                    }

                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    status = "❌ Firestore error: ${e.message}"
                                }
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            status = "❌ Login failed: ${e.message}"
                        }

                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
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

            TextButton(onClick = {
                navController.navigate(Screen.Signup.route)
            }) {
                Text("Don’t have an account? Sign up")
            }

            Spacer(Modifier.height(16.dp))
            Text(status)
        }
    }
}
