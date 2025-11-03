package com.example.skillshare.model

data class Learner(
    val id: Int,
    val name: String,
    val email: String,
    val location: String,
    val bookedSessions: List<Booking> = emptyList()
)
