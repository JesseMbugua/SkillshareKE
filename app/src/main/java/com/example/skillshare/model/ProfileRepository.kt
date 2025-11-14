package com.example.skillshare.model

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Timestamp
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    suspend fun uploadProfileImage(uri: Uri): String {
        val uid = auth.currentUser!!.uid
        val ref = storage.reference.child("profilePictures/$uid/profile.jpg")

        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun saveProfile(
        username: String?,
        displayName: String,
        bio: String?,
        photoUrl: String?
    ) {
        val uid = auth.currentUser!!.uid

        db.runTransaction { transaction ->

            val userRef = db.collection("users").document(uid)
            val usernameRef = username?.let { db.collection("usernames").document(username) }

            val userDoc = transaction.get(userRef)
            val oldUsername = userDoc.getString("username")

            if (username != null && username != oldUsername) {
                val newUsernameDoc = transaction.get(usernameRef!!)

                if (newUsernameDoc.exists()) {
                    throw Exception("Username already taken")
                }

                if (!oldUsername.isNullOrEmpty()) {
                    val oldRef = db.collection("usernames").document(oldUsername)
                    transaction.delete(oldRef)
                }

                // Reserve the new username
                transaction.set(usernameRef, mapOf("uid" to uid))
            }

            val data = mapOf(
                "username" to username,
                "displayName" to displayName,
                "bio" to bio,
                "photoUrl" to photoUrl,
                "updatedAt" to Timestamp.now()
            )
            
            transaction.set(userRef, data, SetOptions.merge())
        }.await()
    }

    suspend fun isUsernameAvailable(username: String): Boolean {
        val usernameRef = db.collection("usernames").document(username)
        val snapshot = usernameRef.get().await()
        return !snapshot.exists()  // true = available, false = taken
    }

}
