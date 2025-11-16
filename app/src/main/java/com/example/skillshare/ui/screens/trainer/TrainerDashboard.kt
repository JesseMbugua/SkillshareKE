package com.example.skillshare.ui.screens.trainer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillshare.navigation.Screen
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import java.util.Locale
import java.util.Date

// ------------------ MODELS ------------------------

data class Transaction(
    val id: Int,
    val title: String,
    val date: String,
    val type: String,
    val status: TransactionStatus,
    val icon: ImageVector
)

enum class TransactionStatus { SUCCESS, FAILED }

// ------------------ SCREEN ------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerDashboard(navController: NavController, trainerId: String) {

    val db = FirebaseFirestore.getInstance()

    var displayName by remember { mutableStateOf("Loading...") }
    var photoUrl by remember { mutableStateOf<String?>(null) }
    var balance by remember { mutableStateOf("0 Ksh") }
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }

    // Fetch all trainer data
    LaunchedEffect(trainerId) {

        // Fetch Profile
        db.collection("users").document(trainerId)
            .addSnapshotListener { doc, _ ->
                if (doc != null && doc.exists()) {
                    displayName = doc.getString("displayName") ?: "Unknown"
                    photoUrl = doc.getString("photoUrl")
                }
            }

        // Fetch Wallet
        db.collection("trainerWallets").document(trainerId)
            .addSnapshotListener { doc, _ ->
                if (doc != null && doc.exists()) {
                    val bal = doc.getDouble("balance") ?: 0.0
                    balance = "${bal.toInt()} Ksh"
                }
            }

        // Fetch Transactions
        db.collection("trainerTransactions")
            .document(trainerId)
            .collection("transactions")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    transactions = snapshot.documents.mapNotNull { tx ->
                        Transaction(
                            id = tx.id.hashCode(),
                            title = tx.getString("buyerName") ?: "Unknown Buyer",
                            date = formatDate(tx.getTimestamp("date")?.toDate()),
                            type = tx.getString("type") ?: "payment",
                            status = if (tx.getString("status") == "success")
                                TransactionStatus.SUCCESS
                            else TransactionStatus.FAILED,
                            icon = Icons.Default.Storefront
                        )
                    }
                }
            }
    }

    var selectedTab by remember { mutableStateOf("Dashboard") }

    Scaffold(
        bottomBar = {
            DashboardBottomNavigation(
                selectedItem = selectedTab,
                onItemSelected = {
                    selectedTab = it
                    when (it) {
                        "Dashboard" -> {}
                        "Profile" -> navController.navigate(Screen.TrainerProfile.route)
                        "Notifications" -> navController.navigate(Screen.Conversations.route)
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            item { Spacer(Modifier.height(16.dp)) }

            item {
                DashboardHeader(
                    username = displayName,
                    profileImageUrl = photoUrl
                )
            }

            item { Spacer(Modifier.height(24.dp)) }

            item {
                BalanceCard(balance = balance)
            }

            item { Spacer(Modifier.height(24.dp)) }

            item {
                ActionButtons(
                    onAddSkill = {
                        navController.navigate(Screen.AddSkill.createRoute(trainerId))
                    },
                    onViewSkills = {
                        navController.navigate(Screen.TrainerSkills.route)
                    }
                )
            }

            item { Spacer(Modifier.height(24.dp)) }

            item { TransactionsHeader() }

            item { Spacer(Modifier.height(16.dp)) }

            if (transactions.isEmpty()) {
                item {
                    Text("No transactions yet.")
                }
            } else {
                items(transactions) { tx ->
                    TransactionItem(tx)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

// ------------------ UI COMPONENTS ------------------------

@Composable
fun DashboardHeader(username: String, profileImageUrl: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Welcome back")
            Text(username, fontWeight = FontWeight.Bold)
        }

        Image(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = "",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun BalanceCard(balance: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(balance, style = MaterialTheme.typography.headlineLarge)
            Text("Balance")
        }
    }
}

@Composable
fun ActionButtons(onAddSkill: () -> Unit, onViewSkills: () -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {

        OutlinedButton(onClick = onAddSkill) {
            Icon(Icons.Default.Add, "")
            Spacer(Modifier.width(8.dp))
            Text("Add Skill")
        }

        OutlinedButton(onClick = onViewSkills) {
            Icon(Icons.Default.List, "")
            Spacer(Modifier.width(8.dp))
            Text("View Skills")
        }
    }
}

@Composable
fun TransactionsHeader() {
    Text("Transactions", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
}

@Composable
fun TransactionItem(tx: Transaction) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(tx.icon, contentDescription = "", modifier = Modifier.size(40.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(tx.title, fontWeight = FontWeight.Bold)
            Text("${tx.date} • ${tx.type}")
        }
        Spacer(Modifier.weight(1f))
        Text(
            tx.status.name,
            color = if (tx.status == TransactionStatus.SUCCESS) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun DashboardBottomNavigation(selectedItem: String, onItemSelected: (String) -> Unit) {
    NavigationBar {
        listOf("Dashboard", "Notifications", "Profile").forEach {
            NavigationBarItem(
                selected = (selectedItem == it),
                onClick = { onItemSelected(it) },
                icon = {
                    when (it) {
                        "Dashboard" -> Icon(Icons.Default.Storefront, "")
                        "Notifications" -> Icon(Icons.Default.Notifications, "")
                        else -> Icon(Icons.Default.Person, "")
                    }
                },
                label = { Text(it) }
            )
        }
    }
}

// ------------------ UTILS ------------------------

fun formatDate(date: Date?): String {
    if (date == null) return "--"
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(date)
}
