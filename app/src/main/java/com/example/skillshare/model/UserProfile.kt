package com.example.skillshare.model

data class UserProfile(
    val username: String? = null,
    val displayName: String = "",
    val bio: String? = null,
    val photoUrl: String? = null
)
