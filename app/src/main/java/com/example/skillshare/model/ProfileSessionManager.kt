package com.example.skillshare.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


object ProfileSessionManager {

    private val auth = FirebaseAuth.getInstance()
    private var profileListener: ListenerRegistration? = null
    private val profileRepo = ProfileRepository()

    private val _currentProfile = MutableStateFlow<UserProfile?>(null)
    val currentProfile: StateFlow<UserProfile?> = _currentProfile

    init {
        // Listen to FirebaseAuth state changes
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                clearSession()
            } else {
                startProfileListener(user.uid)
            }
        }
    }

    // Clear everything when user logs out
    fun clearSession() {
        profileListener?.remove()
        profileListener = null
        _currentProfile.value = null
    }

    // Begin listening to the Firestore profile document
    private fun startProfileListener(uid: String) {
        profileListener?.remove()  // Remove old listener if exists

        profileListener = profileRepo.db.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    _currentProfile.value = UserProfile(
                        username = snapshot.getString("username"),
                        displayName = snapshot.getString("displayName") ?: "",
                        bio = snapshot.getString("bio"),
                        photoUrl = snapshot.getString("photoUrl")
                    )
                }
            }
    }
}
