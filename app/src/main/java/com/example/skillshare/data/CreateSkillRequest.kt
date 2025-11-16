package com.example.skillshare.data

import kotlinx.serialization.Serializable

@Serializable
data class CreateSkillRequest(
    val title: String,
    val description: String,
    val duration: Int,
    val cost: Double,
    val location: String
)
