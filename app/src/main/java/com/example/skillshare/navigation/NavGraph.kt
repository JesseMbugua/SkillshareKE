package com.example.skillshare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.skillshare.ui.screens.details.TrainerDetailScreen
import com.example.skillshare.ui.screens.payment.PaymentScreen
import com.example.skillshare.ui.screens.profiles.LearnerProfileScreen
import com.example.skillshare.ui.screens.profiles.TrainerProfileScreen
import com.example.skillshare.ui.screens.reviews.ReviewScreen
import com.example.skillshare.ui.screens.trainer.SearchScreen
import com.example.skillshare.ui.screens.auth.LoginScreen
import com.example.skillshare.ui.screens.auth.SignupScreen
import com.example.skillshare.ui.screens.trainer.TrainerDashboardScreen
import com.example.skillshare.ui.screens.user.UserDashboardScreen
import com.example.skillshare.ui.screens.trainer.TrainerSkillsScreen
import com.example.skillshare.ui.screens.messaging.ChatScreen
import com.example.skillshare.ui.screens.messaging.ConversationsScreen

// Sealed class for navigation routes
sealed class Screen(
    val route: String,
    val title: String
) {
    object Login : Screen("login", "Login")
    object Signup : Screen("signup", "Signup")
    object Search : Screen("search", "Search")
    object Reviews : Screen("reviews", "Reviews")
    object LearnerProfile : Screen("learner_profile", "Profile")
    object Details : Screen("details", "Details")
    object TrainerProfile : Screen("trainer_profile", "Trainer")
    object Payment : Screen("payment", "Payment")
    object TrainerDashboard : Screen("trainer_dashboard", "Trainer Dashboard")
    object UserDashboard : Screen("user_dashboard", "User Dashboard")
    object Profile : Screen("profile", "Profile")
    object TrainerSkills : Screen("trainer_skills", "Trainer Skills")
    object Chat : Screen("chat", "Chat")
    object Conversations : Screen("conversations", "Conversations")
    object AddSkill : Screen("add_skill", "Add Skill")
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        //  Auth routes
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Signup.route) { SignupScreen(navController) }

        //  Dashboards
        composable(Screen.TrainerDashboard.route) { TrainerDashboardScreen(navController) }
        composable(Screen.UserDashboard.route) { UserDashboardScreen(navController) }

        //  Main App Pages
        composable(Screen.Search.route) { SearchScreen(navController) }
        composable(Screen.Details.route) { TrainerDetailScreen(navController) }
        composable(Screen.Reviews.route) { ReviewScreen(navController) }
        composable(Screen.Payment.route) { PaymentScreen(navController) }

        //  Profile Management - Remove duplicates and point to the correct screens
       // composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.TrainerProfile.route) { TrainerProfileScreen(navController) }
        composable(Screen.LearnerProfile.route) { LearnerProfileScreen(navController) }

        //  Trainer Screens
        composable(Screen.TrainerSkills.route) { TrainerSkillsScreen(navController) }

        //  Messaging
        composable("chat/{conversationId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("conversationId") ?: ""
            ChatScreen(conversationId = id, navController = navController)
        }
        composable(Screen.Conversations.route) { ConversationsScreen(navController) }
    }
}




/*
    @Composable
    fun ProfileScreen(navController: NavHostController) { ... }

    @Composable
    fun TrainerSkillsScreen(navController: NavHostController) { ... }

    @Composable
    fun ManageBookingsScreen(navController: NavHostController) { ... }

    @Composable
    fun ChatScreen(conversationId: String, navController: NavHostController) { ... }

    @Composable
    fun ConversationsScreen(navController: NavHostController) { ... }

    @Composable
    fun TrainerProfileScreen(navController: NavHostController) { ... }
*/

