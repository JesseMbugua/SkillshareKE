package com.example.skillshare.data

import kotlinx.serialization.Serializable

@Serializable
data class Skill(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val duration: Int = 0,
    val cost: Double = 0.0,
    val location: String = ""
)
