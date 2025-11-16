package com.example.skillshare.data

import kotlinx.serialization.Serializable

@Serializable
data class PresignResponse(
    val uploadUrl: String,
    val publicUrl: String,
    val videoId: String,
    val objectKey: String
)
