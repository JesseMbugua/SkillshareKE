package com.example.skillshare.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillshare.model.Skill
import com.example.skillshare.network.SkillApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SkillListViewModel(private val skillApiService: SkillApiService) : ViewModel() {

    private val _skills = MutableStateFlow<List<Skill>>(emptyList())
    val skills: StateFlow<List<Skill>> = _skills

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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
                _skills.value = emptyList() // Clear skills on error
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun addSkill(skill: Skill) {
        // This will call your API and will throw an exception on failure,
        // which will be caught in the AddSkillScreen
        skillApiService.createSkill(skill)
    }
}
