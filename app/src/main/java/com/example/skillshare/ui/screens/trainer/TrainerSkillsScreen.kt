package com.example.skillshare.ui.screens.trainer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerSkillsScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid

    var skills by remember { mutableStateOf(listOf<Skill>()) }
    var loading by remember { mutableStateOf(true) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingSkill by remember { mutableStateOf<Skill?>(null) }

    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("skills").whereEqualTo("trainerId", uid).addSnapshotListener { snapshot, _ ->
                skills = snapshot?.documents?.mapNotNull { doc ->
                    Skill(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        price = doc.getDouble("price") ?: 0.0
                    )
                } ?: emptyList()
                loading = false
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("My Skills", style = MaterialTheme.typography.headlineSmall)
                Button(onClick = { showAddDialog = true }) { Text("Add") }
            }

            Spacer(Modifier.height(12.dp))

            if (loading) Text("Loading...")
            else if (skills.isEmpty()) Text("No skills yet")
            else {
                LazyColumn {
                    itemsIndexed(skills) { index, skill ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(skill.title, style = MaterialTheme.typography.titleMedium)
                                Text(skill.description)
                                Spacer(Modifier.height(8.dp))
                                Row {
                                    Button(onClick = { editingSkill = skill; showAddDialog = true }) { Text("Edit") }
                                    Spacer(Modifier.width(8.dp))
                                    OutlinedButton(onClick = {
                                        // delete
                                        db.collection("skills").document(skill.id).delete()
                                    }) { Text("Delete") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddEditSkillDialog(
            initial = editingSkill,
            onDismiss = { showAddDialog = false; editingSkill = null },
            onSave = { skill ->
                // save to firestore
                val data = hashMapOf(
                    "trainerId" to uid,
                    "title" to skill.title,
                    "description" to skill.description,
                    "price" to skill.price
                )
                if (skill.id.isBlank()) {
                    db.collection("skills").add(data)
                } else {
                    db.collection("skills").document(skill.id).set(data)
                }
                showAddDialog = false
                editingSkill = null
            }
        )
    }
}

data class Skill(val id: String = "", val title: String = "", val description: String = "", val price: Double = 0.0)

@Composable
fun AddEditSkillDialog(initial: Skill?, onDismiss: () -> Unit, onSave: (Skill) -> Unit) {
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var description by remember { mutableStateOf(initial?.description ?: "") }
    var priceText by remember { mutableStateOf(initial?.price?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val price = priceText.toDoubleOrNull() ?: 0.0
                onSave(Skill(initial?.id ?: "", title, description, price))
            }) { Text("Save") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text(if (initial == null) "Add Skill" else "Edit Skill") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                OutlinedTextField(value = priceText, onValueChange = { priceText = it }, label = { Text("Price (Ksh)") }, singleLine = true)
            }
        }
    )
}