package com.example.skillshare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.skillshare.network.RetrofitInstance
import com.example.skillshare.ui.screens.details.TrainerDetailScreen
import com.example.skillshare.ui.screens.skills.AddSkillScreen
import com.example.skillshare.ui.screens.payment.PaymentScreen
import com.example.skillshare.ui.screens.profiles.LearnerProfileScreen
import com.example.skillshare.ui.screens.profiles.TrainerProfileScreen
import com.example.skillshare.ui.screens.reviews.ReviewScreen
import com.example.skillshare.ui.screens.trainer.SearchScreen
import com.example.skillshare.ui.screens.auth.LoginScreen
import com.example.skillshare.ui.screens.auth.SignupScreen
import com.example.skillshare.ui.screens.trainer.TrainerDashboard
import com.example.skillshare.ui.screens.trainer.TrainerSkillsScreen
import com.example.skillshare.ui.screens.user.UserDashboardScreen
import com.example.skillshare.ui.screens.messaging.ChatScreen
import com.example.skillshare.ui.screens.messaging.ConversationsScreen
import com.example.skillshare.viewmodel.SkillListViewModel
import com.example.skillshare.viewmodel.SkillListViewModelFactory

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
    // Create a shared ViewModel scoped to this NavHost.
    val skillListViewModel: SkillListViewModel = viewModel(
        factory = SkillListViewModelFactory(RetrofitInstance.api)
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        //  Auth routes
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Signup.route) { SignupScreen(navController) }

        //  Dashboards
        composable(Screen.TrainerDashboard.route) {
            TrainerDashboard(
                onViewSkills = { navController.navigate(Screen.TrainerSkills.route) },
                onAddSkill = { navController.navigate(Screen.AddSkill.route) }
            )
         }
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
        composable(Screen.TrainerSkills.route) {
            TrainerSkillsScreen(
                onSkillClick = { navController.navigate(Screen.Details.route) },
                onBack = { navController.popBackStack() },
                viewModel = skillListViewModel // Pass the shared ViewModel
            )
        }
        composable(Screen.AddSkill.route) {
            AddSkillScreen(
                navController = navController,
                viewModel = skillListViewModel // Pass the shared ViewModel
            )
        }

        //  Messaging
        composable("chat/{conversationId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("conversationId") ?: ""
            ChatScreen(conversationId = id, navController = navController)
        }
        composable(Screen.Conversations.route) { ConversationsScreen(navController) }
    }
}
