package com.example.skillshare.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillshare.data.CreateSkillRequest
import com.example.skillshare.data.Skill
import com.example.skillshare.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SkillListViewModel : ViewModel() {

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

    fun addSkill(title: String, description: String, duration: Int, cost: Double, location: String, trainerId: String) {
        viewModelScope.launch {
            try {
                val request = CreateSkillRequest(title, description, duration, cost, location, trainerId)
                skillApiService.createSkill(request)
                _addSkillResult.emit(Result.success(Unit))
                fetchSkills() // Refresh the list after adding
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("SkillListViewModel", "HttpException: ${errorBody ?: "Unknown error"}", e)
                _addSkillResult.emit(Result.failure(Exception("Server Error: ${errorBody ?: "Please try again"}")))
            } catch (e: IOException) {
                Log.e("SkillListViewModel", "IOException: Network error", e)
                _addSkillResult.emit(Result.failure(Exception("Network Error: Please check your connection.")))
            } catch (e: Exception) {
                Log.e("SkillListViewModel", "Failed to add skill", e)
                _addSkillResult.emit(Result.failure(e))
            }
        }
    }
}
