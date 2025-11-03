package com.example.skillshare.model

data class Payment(
    val id: Int,
    val bookingId: Int,
    val amount: Double,
    val method: String, // e.g., "M-Pesa", "Card", "Cash"
    val status: String, // "Pending", "Completed", "Failed"
    val date: String
)
