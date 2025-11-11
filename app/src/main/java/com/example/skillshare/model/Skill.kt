package com.example.skillshare.model

import kotlinx.serialization.Serializable

@Serializable
data class Skill(
    val id: String,
    val title: String,
    val description: String,
    val duration: Int,
    val cost: Double,
    val location: String
)
