package com.example.skillshare.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _totalUsers = MutableStateFlow(0)
    val totalUsers: StateFlow<Int> = _totalUsers

    private val _totalTrainers = MutableStateFlow(0)
    val totalTrainers: StateFlow<Int> = _totalTrainers

    private val _totalRevenue = MutableStateFlow(0.0)
    val totalRevenue: StateFlow<Double> = _totalRevenue

    private val _users = MutableStateFlow<List<AdminUser>>(emptyList())
    val users: StateFlow<List<AdminUser>> = _users

    init {
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            db.collection("users").get().addOnSuccessListener { snapshot ->
                val all = snapshot.documents
                _totalUsers.value = all.size
                _totalTrainers.value = all.count { it.getString("role") == "trainer" }

                _users.value = all.map {
                    AdminUser(
                        id = it.id,
                        name = it.getString("displayName") ?: "No name",
                        role = it.getString("role") ?: "",
                        email = it.getString("email") ?: "",
                        banned = it.getBoolean("banned") ?: false
                    )
                }
            }

            db.collection("payments")
                .get()
                .addOnSuccessListener { snapshot ->
                    val revenue = snapshot.documents.sumOf { it.getDouble("amount") ?: 0.0 }
                    _totalRevenue.value = revenue
                }
        }
    }

    fun setUserBanStatus(userId: String, banned: Boolean) {
        db.collection("users").document(userId)
            .update("banned", banned)
    }

    fun deleteSkill(skillId: String) {
        db.collection("skills").document(skillId).delete()
    }
}
