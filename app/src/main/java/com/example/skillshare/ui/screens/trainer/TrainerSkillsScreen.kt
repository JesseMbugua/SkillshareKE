package com.example.skillshare.ui.screens.trainer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillshare.viewmodel.SkillListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


// Data class is correct
data class Skill(val id: String = "", val title: String = "", val description: String = "", val price: Double = 0.0,val tutorialVideoUrl: String = "")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerSkillsScreen(onSkillClick: (String) -> Unit,
                        onBack: () -> Unit,
                        viewModel: SkillListViewModel,
                        modifier: Modifier = Modifier,
                        navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid

    var skills by remember { mutableStateOf(listOf<Skill>()) }
    var loading by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var editingSkill by remember { mutableStateOf<Skill?>(null) }

    // Real-time listener for skills
    LaunchedEffect(uid) {
        if (uid != null) {
            val listener = db.collection("skills").whereEqualTo("trainerId", uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        loading = false
                        return@addSnapshotListener
                    }
                    skills = snapshot?.toObjects(Skill::class.java)?.mapIndexed { index, skill ->
                        skill.copy(id = snapshot.documents[index].id)
                    } ?: emptyList()
                    loading = false
                }
        } else {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage My Skills") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Skill") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Skill") },
                onClick = {
                    editingSkill = null
                    showDialog = true
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (skills.isEmpty()) {
                Text("You haven't added any skills yet.", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(skills, key = { it.id }) { skill ->
                        SkillCard(
                            skill = skill,
                            onEdit = {
                                editingSkill = skill
                                showDialog = true
                            },
                            onDelete = { db.collection("skills").document(skill.id).delete() }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddEditSkillDialog(
            initialSkill = editingSkill,
            onDismiss = { showDialog = false },
            onSave = { skillToSave ->
                // IMPORTANT: Make sure you save the trainerId
                val data = hashMapOf(
                    "trainerId" to uid,
                    "title" to skillToSave.title,
                    "description" to skillToSave.description,
                    "price" to skillToSave.price,
                    "tutorialVideoUrl" to skillToSave.tutorialVideoUrl
                )
                if (skillToSave.id.isBlank()) {
                    db.collection("skills").add(data)
                } else {
                    db.collection("skills").document(skillToSave.id).set(data)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun SkillCard(skill: Skill, onEdit: () -> Unit, onDelete: () -> Unit) {

    val context = LocalContext.current

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(skill.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(skill.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Text("Ksh ${"%,.2f".format(skill.price)}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                if (skill.tutorialVideoUrl.isNotBlank()) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(skill.tutorialVideoUrl))
                            // This line will now work correctly
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer, contentColor = MaterialTheme.colorScheme.onTertiaryContainer)
                    ) {
                        Icon(Icons.Default.PlayCircleOutline, contentDescription = "Watch Tutorial", modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Watch Tutorial")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                TextButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Edit")
                }
                Spacer(Modifier.width(8.dp))
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun AddEditSkillDialog(initialSkill: Skill?, onDismiss: () -> Unit, onSave: (Skill) -> Unit) {
    var title by remember { mutableStateOf(initialSkill?.title ?: "") }
    var description by remember { mutableStateOf(initialSkill?.description ?: "") }
    var priceText by remember(initialSkill) { mutableStateOf(initialSkill?.price?.takeIf { it > 0 }?.toString() ?: "") }
    var videoUrl by remember { mutableStateOf(initialSkill?.tutorialVideoUrl ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialSkill == null) "Add New Skill" else "Edit Skill") },
        text = {
            LazyColumn {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Skill Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            label = { Text("Price (Ksh)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = videoUrl,
                            onValueChange = { videoUrl = it },
                            label = { Text("Tutorial Video URL (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val price = priceText.toDoubleOrNull() ?: 0.0
                onSave(
                    Skill(
                        id = initialSkill?.id ?: "",
                        title = title,
                        description = description,
                        price = price,
                        tutorialVideoUrl = videoUrl
                    )
                )
            }) { Text("Save") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
