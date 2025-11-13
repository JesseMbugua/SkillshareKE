package com.example.skillshare.ui.screens.messaging

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.skillshare.navigation.Screen


@Composable
fun ConversationsScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var convos by remember { mutableStateOf(listOf<Map<String, Any>>()) }

    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("conversations")
                .whereArrayContains("members", uid)
                .addSnapshotListener { snap, _ ->
                    convos = snap?.documents?.mapNotNull { it.data?.plus("id" to it.id) } ?: emptyList()
                }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Messages", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            LazyColumn {
                items(convos) { c ->
                    val id = c["id"] as? String ?: ""
                    val title = c["title"] as? String ?: "Conversation"
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("${Screen.Payment.route}/$id") // replace with chat route
                        }) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(title)
                            Text("Members: ${(c["members"] as? List<*>)?.size ?: 0}")
                        }
                    }
                }
            }
        }
    }
}
