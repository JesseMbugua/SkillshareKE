package com.example.skillshare.data

import kotlinx.serialization.Serializable

@Serializable
data class PresignRequest(
    val trainerId: String,
    val filename: String,
    val contentType: String
)
