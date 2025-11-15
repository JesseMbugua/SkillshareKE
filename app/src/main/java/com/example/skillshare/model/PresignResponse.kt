package com.example.skillshare.com.example.skillshare.model

data class PresignResponse(
    val uploadUrl: String,
    val publicUrl: String,
    val videoId: String,
    val objectKey: String
)
