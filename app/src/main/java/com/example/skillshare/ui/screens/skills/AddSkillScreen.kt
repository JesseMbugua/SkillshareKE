package com.example.skillshare.ui.screens.skills

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillshare.model.Skill
import com.example.skillshare.viewmodel.SkillListViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSkillScreen(navController: NavController, viewModel: SkillListViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var skillName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add New Skill") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            OutlinedTextField(
                value = skillName,
                onValueChange = { skillName = it },
                label = { Text("Skill Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (in minutes)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val durationInt = duration.toIntOrNull()
                        val costDouble = price.toDoubleOrNull()

                        if (skillName.isBlank() || description.isBlank() || duration.isBlank() || price.isBlank() || location.isBlank()) {
                            snackbarHostState.showSnackbar("Please fill all fields.")
                            return@launch
                        }

                        if (durationInt == null) {
                            snackbarHostState.showSnackbar("Please enter a valid number for duration.")
                            return@launch
                        }

                        if (costDouble == null) {
                            snackbarHostState.showSnackbar("Please enter a valid number for price.")
                            return@launch
                        }

                        val newSkill = Skill(
                            title = skillName,
                            description = description,
                            duration = durationInt,
                            cost = costDouble,
                            location = location
                        )

                        try {
                            viewModel.addSkill(newSkill)
                            snackbarHostState.showSnackbar("Skill added successfully!")

                            // Refresh the list and navigate back
                            viewModel.fetchSkills()
                            navController.popBackStack()

                        } catch (e: HttpException) {
                            val errorBody = e.response()?.errorBody()?.string()
                            snackbarHostState.showSnackbar("Error: ${errorBody ?: "An unknown server error occurred"}")
                        } catch (e: IOException) {
                            snackbarHostState.showSnackbar("Network error: Please check your connection.")
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("An unexpected error occurred: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Skill")
            }
        }
    }
}
