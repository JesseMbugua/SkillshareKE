package com.example.skillshare.model

import android.net.Uri

class ProfileManager(
    private val repo: ProfileRepository = ProfileRepository()
) {

    suspend fun updateUserProfile(
        username: String?,
        displayName: String,
        bio: String?,
        imageUri: Uri?
    ) {
        val photoUrl = if (imageUri != null) {
            repo.uploadProfileImage(imageUri)
        } else {
            null
        }

        repo.saveProfile(
            username = username,
            displayName = displayName,
            bio = bio,
            photoUrl = photoUrl
        )
    }
    // Public method to check username availability
    suspend fun isUsernameAvailable(username: String): Boolean {
        return repo.isUsernameAvailable(username)
    }

}
