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

    init {
        fetchSkills()
    }

    private fun fetchSkills() {
        viewModelScope.launch {
            try {
                _skills.value = skillApiService.getSkills()
            } catch (e: Exception) {
                Log.e("SkillListViewModel", "Failed to fetch skills", e)
            }
        }
    }
}
