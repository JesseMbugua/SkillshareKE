package com.example.skillshare.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.skillshare.navigation.Screen
import at.favre.lib.crypto.bcrypt.BCrypt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("User") }
    var expandedRole by remember { mutableStateOf(false) }

    var selectedCounty by remember { mutableStateOf("") }
    var expandedCounty by remember { mutableStateOf(false) }

    var status by remember { mutableStateOf("") }

    val counties = listOf<String>("Baringo", "Bomet", "Bungoma", "Busia", "Elgeyo-Marakwet", "Embu",
        "Garissa", "Homa Bay", "Isiolo", "Kajiado", "Kakamega", "Kericho",
        "Kiambu", "Kilifi", "Kirinyaga", "Kisii", "Kisumu", "Kitui", "Kwale",
        "Laikipia", "Lamu", "Machakos", "Makueni", "Mandera", "Marsabit",
        "Meru", "Migori", "Mombasa", "Murang’a", "Nairobi", "Nakuru",
        "Nandi", "Narok", "Nyamira", "Nyandarua", "Nyeri", "Samburu",
        "Siaya", "Taita-Taveta", "Tana River", "Tharaka-Nithi", "Trans Nzoia",
        "Turkana", "Uasin Gishu", "Vihiga", "Wajir", "West Pokot"
    )

    val roles = listOf("User", "Trainer")

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

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            // Role dropdown
            ExposedDropdownMenuBox(expanded = expandedRole, onExpandedChange = { expandedRole = !expandedRole }) {
                OutlinedTextField(
                    value = selectedRole,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Role") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRole) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = expandedRole, onDismissRequest = { expandedRole = false }) {
                    roles.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role) },
                            onClick = {
                                selectedRole = role
                                expandedRole = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // County dropdown
            ExposedDropdownMenuBox(expanded = expandedCounty, onExpandedChange = { expandedCounty = !expandedCounty }) {
                OutlinedTextField(
                    value = selectedCounty,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select County") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCounty) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = expandedCounty, onDismissRequest = { expandedCounty = false }) {
                    counties.forEach { county ->
                        DropdownMenuItem(
                            text = { Text(county) },
                            onClick = {
                                selectedCounty = county
                                expandedCounty = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || selectedCounty.isEmpty()) {
                        status = "Please fill all fields"
                        return@Button
                    }

                    // *** HASH PASSWORD HERE ***
                    val hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray())

                    val user = hashMapOf(
                        "name" to name,
                        "email" to email.lowercase(),
                        "phone" to phone,
                        "passwordHash" to hashed,   // <<-- use hashed
                        "county" to selectedCounty,
                        "role" to selectedRole.lowercase(),
                        "createdAt" to System.currentTimeMillis()
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