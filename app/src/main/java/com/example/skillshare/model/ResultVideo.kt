package com.example.skillshare.com.example.skillshare.model

sealed class ResultVideo {
    data class Success(
        val videoId: String,
        val videoUrl: String,
        val thumbnailUrl: String?
    ) : ResultVideo()

    data class Error(val message: String) : ResultVideo()
}
