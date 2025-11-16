package com.example.skillshare.viewmodel

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skillshare.data.CreateSkillRequest
import com.example.skillshare.data.PresignRequest
import com.example.skillshare.data.Skill
import com.example.skillshare.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException

// The ViewModel now takes Application in its constructor to get access to the ContentResolver
class SkillListViewModel(private val application: Application) : ViewModel() {

    private val skillApiService = RetrofitInstance.api

    private val _skills = MutableStateFlow<List<Skill>>(emptyList())
    val skills = _skills.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _addSkillResult = MutableSharedFlow<Result<Unit>>()
    val addSkillResult = _addSkillResult.asSharedFlow()

    init {
        fetchSkills()
    }

    fun fetchSkills() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _skills.value = skillApiService.getSkills()
            } catch (e: Exception) {
                Log.e("SkillListViewModel", "Failed to fetch skills", e)
                _skills.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Updated addSkill function
    fun addSkill(
        title: String,
        description: String,
        duration: Int,
        cost: Double,
        location: String,
        trainerId: String,
        videoUri: Uri?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // If a video is selected, upload it first
                val videoUrl = if (videoUri != null) {
                    uploadVideoAndGetUrl(trainerId, videoUri)
                } else {
                    null // No video to upload
                }

                // Now create the skill with the video URL (if available)
                val request = CreateSkillRequest(
                    title = title,
                    description = description,
                    duration = duration,
                    cost = cost,
                    location = location,
                    trainerId = trainerId,
                    videoUrl = videoUrl
                )
                skillApiService.createSkill(request)
                _addSkillResult.emit(Result.success(Unit))
                fetchSkills() // Refresh the list

            } catch (e: Exception) {
                // Catch any exception from the upload or skill creation process
                Log.e("SkillListViewModel", "Failed to add skill with video", e)
                val errorMessage = when (e) {
                    is HttpException -> "Server Error: ${e.message()}"
                    is IOException -> "Network Error: Please check your connection."
                    else -> e.message ?: "An unknown error occurred"
                }
                _addSkillResult.emit(Result.failure(Exception(errorMessage)))
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Orchestration function for video upload
    private suspend fun uploadVideoAndGetUrl(trainerId: String, videoUri: Uri): String {
        // 1. Get metadata from URI
        val fileName = getFileName(videoUri) ?: "video.mp4"
        val contentType = application.contentResolver.getType(videoUri) ?: "video/*"

        // 2. Get a pre-signed URL from our server
        val presignRequest = PresignRequest(trainerId, fileName, contentType)
        val presignResponse = skillApiService.getPresignedUrl(presignRequest)

        // 3. Create a RequestBody from the video file's content
        val inputStream = application.contentResolver.openInputStream(videoUri)
            ?: throw IOException("Could not open input stream for video URI")
        val requestBody = inputStream.readBytes().toRequestBody(contentType.toMediaTypeOrNull())
        
        inputStream.close() // Close the stream

        // 4. Upload the video to the pre-signed URL
        val uploadResponse = skillApiService.uploadVideo(presignResponse.uploadUrl, requestBody)
        if (!uploadResponse.isSuccessful) {
            throw IOException("Video upload failed with code: ${uploadResponse.code()}")
        }

        // 5. Return the public URL to be saved with the skill
        return presignResponse.publicUrl
    }

    // Helper to get file name from content URI
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = application.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) {
                        result = it.getString(columnIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }
}

// A Factory is needed to create the ViewModel with the Application parameter
class SkillListViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SkillListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SkillListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
