package com.example.skillshare.ui.screens.admin

data class AdminUser(
    val id: String,
    val name: String,
    val role: String,
    val email: String,
    val banned: Boolean
)
