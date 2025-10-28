package com.example.skillshare.model

data class Booking(
    val id: Int,
    val trainer: Trainer,
    val date: String,
    val status: String
)