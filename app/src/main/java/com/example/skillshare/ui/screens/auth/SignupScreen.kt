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

@Composable
fun SignupScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var county by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

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
                    } else {
                        val user = hashMapOf(
                            "name" to name,
                            "email" to email.lowercase(),
                            "phone" to phone,
                            "password" to password,
                            "county" to county
                        )

                        db.collection("users")
                            .add(user)
                            .addOnSuccessListener {
                                status = "Account created!"
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Signup.route) { inclusive = true }
                                }
                            }
                            .addOnFailureListener { e ->
                                status = "Error: ${e.message}"
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
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
