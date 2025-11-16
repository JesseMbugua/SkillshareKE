package com.example.skillshare.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()

    var totalUsers by remember { mutableStateOf(0) }
    var totalTrainers by remember { mutableStateOf(0) }
    var totalRevenue by remember { mutableStateOf(0.0) }

    var userList by remember { mutableStateOf<List<Triple<String, String, Boolean>>>(emptyList()) }

    // Load data once
    LaunchedEffect(Unit) {

        // Watch users live
        db.collection("users")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    totalUsers = snapshot.size()
                    userList = snapshot.documents.map { doc ->
                        Triple(
                            doc.id,
                            doc.getString("email") ?: "unknown",
                            safeBoolean(doc.get("banned"))
                        )
                    }
                }
            }

        // Watch trainers
        db.collection("users")
            .whereEqualTo("role", "trainer")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    totalTrainers = snapshot.size()
                }
            }

        // Watch global revenue
        db.collection("globalStats").document("payments")
            .addSnapshotListener { doc, _ ->
                totalRevenue = doc?.getDouble("revenue") ?: 0.0
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Admin Dashboard") })
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AdminStatCard("Users", totalUsers.toString(), modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                AdminStatCard("Trainers", totalTrainers.toString(), modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                AdminStatCard("Revenue", "Ksh ${totalRevenue.toInt()}", modifier = Modifier.weight(1f))
            }

            Text(
                "Users",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(userList) { (id, email, banned) ->
                    UserRow(
                        userId = id,
                        email = email,
                        banned = banned,
                        onBanToggle = {
                            db.collection("users").document(id)
                                .update("banned", !banned)
                        },
                        onDelete = {
                            db.collection("users").document(id)
                                .delete()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(90.dp)
    ) {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontWeight = FontWeight.Bold)
            Text(value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

fun safeBoolean(value: Any?): Boolean {
    return when (value) {
        is Boolean -> value
        is String -> value.equals("true", ignoreCase = true)
        is Number -> value.toInt() == 1
        else -> false
    }
}

@Composable
fun UserRow(
    userId: String,
    email: String,
    banned: Boolean,
    onBanToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            if (banned) MaterialTheme.colorScheme.errorContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    email,
                    fontWeight = FontWeight.SemiBold,
                    color = if (banned)
                        MaterialTheme.colorScheme.onErrorContainer
                    else MaterialTheme.colorScheme.onSurface
                )
                Text("ID: $userId", fontSize = MaterialTheme.typography.bodySmall.fontSize)

                if (banned) {
                    Text(
                        "BANNED",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {

                Button(
                    onClick = onBanToggle,
                    colors = ButtonDefaults.buttonColors(
                        if (banned)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(if (banned) "Unban" else "Ban")
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete user",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
