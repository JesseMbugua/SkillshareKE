package com.example.skillshare.ui.screens.trainer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Storefront

import androidx.navigation.NavController
import com.example.skillshare.navigation.Screen
import com.example.skillshare.ui.theme.SkillshareTheme
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth


data class Transaction(
    val id: Int,
    val title: String,
    val date: String,
    val type: String,
    val status: TransactionStatus,
    val icon: ImageVector
)

enum class TransactionStatus {
    SUCCESS, FAILED
}

val dummyTransactions = listOf(
    Transaction(1, "Mary Akinyi", "November 4th, 2025", "Payment", TransactionStatus.SUCCESS, Icons.Default.Storefront),
    Transaction(2, "Joel Karanja", "October 31st, 2024", "Transfer", TransactionStatus.SUCCESS, Icons.Default.SyncAlt),
    Transaction(3, "Stephanie Joy", "October 21st, 2024", "Payment", TransactionStatus.FAILED, Icons.Default.School)
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerDashboard(navController: NavController, trainerId: String) {
    var selectedBottomNavItem by remember { mutableStateOf("Dashboard") }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        bottomBar = {
            DashboardBottomNavigation(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { screenName ->
                    selectedBottomNavItem = screenName

                    when (screenName) {
                        "Dashboard" -> navController.navigate(Screen.TrainerDashboard.createRoute(trainerId)) { launchSingleTop = true }
                        "Profile" -> navController.navigate(Screen.TrainerProfile.route) { launchSingleTop = true }
                        "Notifications" -> navController.navigate(Screen.Conversations.route) { launchSingleTop = true }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                DashboardHeader(username = "Kimberly Wangari", profileImageUrl = null)
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                BalanceCard(balance = "5,500.50 Ksh")
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                ActionButtons(
                    onAddSkill = {
                        // Use the reliable trainerId passed as a parameter
                        val route = Screen.AddSkill.createRoute(trainerId)
                        navController.navigate(route)
                    },
                    onViewSkills = { navController.navigate(Screen.TrainerSkills.route) }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                TransactionsHeader()
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(dummyTransactions) { transaction ->
                TransactionItem(transaction = transaction)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DashboardHeader(username: String, profileImageUrl: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = username,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = "Priority",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Image(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}

@Composable
fun BalanceCard(balance: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = balance,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Balance",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun ActionButtons(onAddSkill: () -> Unit, onViewSkills: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        ActionButton(icon = Icons.Default.Add, label = "Add Skill", onClick = onAddSkill)
        ActionButton(icon = Icons.Default.List, label = "View Skills", onClick = onViewSkills)
    }
}

@Composable
fun ActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedButton(
            onClick = onClick,
            shape = CircleShape,
            modifier = Modifier.size(64.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            border = null
        ) {
            Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun TransactionsHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Transactions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(
            text = "Sort by: Latest",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val statusColor = if (transaction.status == TransactionStatus.SUCCESS) Color(0xFF388E3C) else MaterialTheme.colorScheme.error
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = transaction.icon,
            contentDescription = transaction.title,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = transaction.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Text(text = "${transaction.date} • ${transaction.type}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Surface(shape = RoundedCornerShape(12.dp), color = statusColor.copy(alpha = 0.1f)) {
            Text(
                text = transaction.status.name.lowercase().replaceFirstChar { it.uppercase() },
                color = statusColor,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun DashboardBottomNavigation(selectedItem: String, onItemSelected: (String) -> Unit) {

    val items = listOf("Dashboard", "Transfer", "Notifications", "Profile")
    val icons = mapOf(
        "Dashboard" to Icons.Outlined.Home,
        "Transfer" to Icons.Default.SyncAlt,
        "Notifications" to Icons.Outlined.ChatBubbleOutline,
        "Profile" to Icons.Outlined.Person
    )

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(icons[screen]!!, contentDescription = screen) },
                label = { Text(screen) },
                selected = selectedItem == screen,
                onClick = { onItemSelected(screen) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {

}

annotation class TrainerDashboard
