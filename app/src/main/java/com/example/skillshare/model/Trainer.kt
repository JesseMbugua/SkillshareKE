package com.example.skillshare.model

data class Trainer(
    val id: Int,
    val name: String,
    val skill: String,
    val location: String,
    val rating: Double,
    val pricePerHour: Int,
    val imageUr: String,
    val bio: String
)