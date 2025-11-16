package com.example.skillshare.com.example.skillshare.ui.screens.admin

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun currentUserIsAdmin(): Boolean {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return false
    val doc = FirebaseFirestore.getInstance().collection("users").document(uid).get().await()
    return doc.exists() && doc.getString("role") == "admin"
}
