package com.example.skillshare.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.skillshare.navigation.Screen

@Composable
fun BottomNavigationBar(currentRoute: String, onNavigate: (String) -> Unit) {
    val items = listOf(
        Screen.Search,
        Screen.Reviews,
        Screen.LearnerProfile
    )

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = { onNavigate(screen.route) },
                icon = { }
            )
        }
    }
}
