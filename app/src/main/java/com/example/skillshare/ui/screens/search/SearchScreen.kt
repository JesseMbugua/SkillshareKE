package com.example.skillshare.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skillshare.model.Trainer
import com.example.skillshare.navigation.Screen

@Composable
fun SearchScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }

    // 🧑‍🏫 Sample trainer data
    val trainers = listOf(
        Trainer(1, "Jane Doe", "Painting", "Nairobi", 4.8, 1500, "", "Experienced painter specializing in creative art."),
        Trainer(2, "Alex Kim", "Photography", "Mombasa", 4.6, 1200, "", "Professional photographer for 5 years."),
        Trainer(3, "Maria Njeri", "Cooking", "Kisumu", 4.9, 1800, "", "Teaches Kenyan traditional dishes.")
    )

    // 🔍 Filter trainers by search query
    val filtered = trainers.filter {
        it.name.contains(query, ignoreCase = true) ||
                it.skill.contains(query, ignoreCase = true) ||
                it.location.contains(query, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // 🔎 Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search by skill, trainer, or location") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.run {
                colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
            }
        )

        Spacer(Modifier.height(16.dp))

        // 🧱 Trainer Boxes
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filtered.size) { index ->
                TrainerCard(filtered[index], onClick = {
                    navController.navigate(Screen.Details.route)
                })
            }
        }
    }
}

@Composable
fun TrainerCard(trainer: Trainer, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🖼️ Placeholder box for image
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = trainer.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = trainer.skill,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "KES ${trainer.pricePerHour}/hr",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = "⭐ ${trainer.rating}",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
