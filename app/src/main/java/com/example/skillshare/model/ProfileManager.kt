package com.example.skillshare.model

import android.net.Uri

class ProfileManager(
    private val repo: ProfileRepository = ProfileRepository()
) {

    // Update profile with structured result handling
    suspend fun updateUserProfile(
        username: String?,
        displayName: String,
        bio: String?,
        imageUri: Uri?
    ): ProfileResult {
        return try {
            val photoUrl = if (imageUri != null) {
                repo.uploadProfileImage(imageUri)
            } else null

            repo.saveProfile(
                username = username,
                displayName = displayName,
                bio = bio,
                photoUrl = photoUrl
            )

            ProfileResult.Success
        } catch (e: Exception) {
            ProfileResult.Error(e.message ?: "Unknown error")
        }
    }

    // Check availability using structured response later if needed
    suspend fun isUsernameAvailable(username: String): Boolean {
        return repo.isUsernameAvailable(username)
    }

    // Get profile as data class
    suspend fun getUserProfile(): UserProfile? {
        return repo.getUserProfile()
    }

    // Allow deleting picture
    suspend fun deleteProfilePicture(): ProfileResult {
        return try {
            repo.deleteOldProfilePicture()
            ProfileResult.Success
        } catch (e: Exception) {
            ProfileResult.Error(e.message ?: "Failed to delete profile picture")
        }
    }
    // Returns live profile via session manager
    fun getCurrentProfileFlow() = ProfileSessionManager.currentProfile

    fun isUser(): Boolean {
        return ProfileSessionManager.currentProfile.value?.role == "user"
    }

    fun isTrainer(): Boolean {
        return ProfileSessionManager.currentProfile.value?.role == "trainer"
    }

    fun isAdmin(): Boolean {
        return ProfileSessionManager.currentProfile.value?.role == "admin"
    }

    suspend fun setUserRole(uid: String, newRole: String): ProfileResult {
        return try {
            repo.updateUserRole(uid, newRole)
            ProfileResult.Success
        } catch (e: Exception) {
            ProfileResult.Error(e.message ?: "Failed to update role")
        }
    }



}
