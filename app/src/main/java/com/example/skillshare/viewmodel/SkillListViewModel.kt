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
import com.example.skillshare.data.PresignResponse
import com.example.skillshare.data.Skill
import com.example.skillshare.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException

class SkillListViewModel(private val application: Application) : ViewModel() {

    private val skillApiService = RetrofitInstance.api

    private val _skills = MutableStateFlow<List<Skill>>(emptyList())
    val skills: StateFlow<List<Skill>> = _skills.asStateFlow()

    // This holds the currently viewed skill
    private val _selectedSkill = MutableStateFlow<Skill?>(null)
    val selectedSkill: StateFlow<Skill?> = _selectedSkill.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _addSkillResult = MutableSharedFlow<Result<Unit>>()
    val addSkillResult = _addSkillResult.asSharedFlow()

    init {
        loadSkills() // Load all skills initially
    }

    fun loadSkills(
        trainerId: String? = null,
        price: Double? = null,
        location: String? = null,
        email: String? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _skills.value = skillApiService.getSkills(
                    trainerId = trainerId,
                    price = price,
                    location = location,
                    email = email
                )
            } catch (e: Exception) {
                Log.e("SkillListViewModel", "Failed to fetch skills", e)
                _skills.value = emptyList() // Reset on error
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Loads a single skill by its ID. It first tries to find the skill in the
     * locally cached list. If not found, it fetches it from the API.
     */
    fun loadSkillById(skillId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedSkill.value = null // Clear previous skill
            try {
                // Prefer local version if available
                val localSkill = _skills.value.find { it.id == skillId }
                if (localSkill != null) {
                    _selectedSkill.value = localSkill
                } else {
                    // Fetch from network if not found locally
                    _selectedSkill.value = skillApiService.getSkill(skillId)
                }
            } catch (e: Exception) {
                Log.e("SkillListViewModel", "Failed to load skill $skillId", e)
                // Optionally expose error to UI
            } finally {
                _isLoading.value = false
            }
        }
    }

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
                val videoUrl = if (videoUri != null) {
                    uploadVideoAndGetUrl(trainerId, videoUri)
                } else {
                    null
                }

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
                loadSkills() // Refresh the master list

            } catch (e: Exception) {
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

    private suspend fun uploadVideoAndGetUrl(trainerId: String, videoUri: Uri): String {
        val fileName = getFileName(videoUri) ?: "video.mp4"
        val contentType = application.contentResolver.getType(videoUri) ?: "video/*"

        val presignRequest = PresignRequest(trainerId, fileName, contentType)
        val presignResponse = skillApiService.getPresignedUrl(presignRequest)

        val inputStream = application.contentResolver.openInputStream(videoUri)
            ?: throw IOException("Could not open input stream for video URI")
        val requestBody = inputStream.readBytes().toRequestBody(contentType.toMediaTypeOrNull())

        inputStream.close()

        val uploadResponse = skillApiService.uploadVideo(presignResponse.uploadUrl, requestBody)
        if (!uploadResponse.isSuccessful) {
            throw IOException("Video upload failed with code: ${uploadResponse.code()}")
        }

        // The fix is to return the permanent, public URL, not the temporary upload URL.
        return presignResponse.publicUrl
    }

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

class SkillListViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SkillListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SkillListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
