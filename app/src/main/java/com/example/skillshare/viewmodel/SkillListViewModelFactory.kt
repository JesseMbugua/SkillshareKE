package com.example.skillshare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skillshare.network.SkillApiService

class SkillListViewModelFactory(private val skillApiService: SkillApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SkillListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SkillListViewModel(skillApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
