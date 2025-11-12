package com.example.skillshare.ui.screens.auth

import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillshare.navigation.Screen
import at.favre.lib.crypto.bcrypt.BCrypt

@Composable
fun SignupScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var county by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Create Account", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone (+254...)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(value = county, onValueChange = { county = it }, label = { Text("County") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || county.isEmpty()) {
                        status = "Please fill all fields"
                        return@Button
                    }

                    loading = true
                    val emailLower = email.trim().lowercase()


                    db.collection("users")
                        .whereEqualTo("email", emailLower)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (!documents.isEmpty) {
                                loading = false
                                status = " Email already exists"
                            } else{
                                val bcryptHash = BCrypt.withDefaults().hashToString(12, password.toCharArray())

                                val user = hashMapOf(
                                    "name" to name.trim(),
                                    "email" to emailLower,
                                    "phone" to phone.trim(),
                                    "passwordHash" to bcryptHash,
                                    "county" to county.trim(),
                                    "role" to "learner",
                                    "createdAt" to System.currentTimeMillis()
                                )
                                db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener {
                                        loading = false
                                        status = "Account created!"
                                        navController.navigate(Screen.Login.route) {
                                            popUpTo(Screen.Signup.route) { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        loading = false
                                        status = "Error: ${e.message}"
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            loading = false
                            status = "Error checking email: ${e.message}"
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text(if (loading) "Please wait..." else "Sign Up")
            }

            Spacer(Modifier.height(16.dp))
            TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Text("Already have an account? Log in")
            }

            Spacer(Modifier.height(16.dp))
            Text(status)
        }
    }
}
