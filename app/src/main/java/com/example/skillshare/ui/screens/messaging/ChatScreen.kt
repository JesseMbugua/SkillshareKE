package com.example.skillshare.ui.screens.messaging

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ChatScreen(conversationId: String, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var messages by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var newMessage by remember { mutableStateOf("") }

    LaunchedEffect(conversationId) {
        db.collection("conversations").document(conversationId).collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snap, _ ->
                messages = snap?.documents?.mapNotNull { document ->

                    document.data?.toMutableMap()?.apply {
                        put("id", document.id)
                    }
                } ?: emptyList()
            }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(12.dp)) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(messages) { m ->
                    val from = m["fromName"] as? String ?: "User"
                    val text = m["text"] as? String ?: ""
                    Text("$from: $text", modifier = Modifier.padding(vertical = 4.dp))
                }
            }

            Row {
                OutlinedTextField(value = newMessage, onValueChange = { newMessage = it }, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    if (newMessage.isNotBlank()) {
                        val msg = hashMapOf(
                            "fromId" to uid,
                            "fromName" to (FirebaseAuth.getInstance().currentUser?.displayName ?: "You"),
                            "text" to newMessage,

                            Pair("timestamp", com.google.firebase.Timestamp.now())
                        )
                        db.collection("conversations").document(conversationId).collection("messages").add(msg)
                        newMessage = ""
                    }
                }) { Text("Send") }
            }
        }
    }
}

