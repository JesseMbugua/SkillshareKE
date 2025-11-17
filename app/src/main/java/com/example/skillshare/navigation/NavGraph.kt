package com.example.skillshare.navigation
// package com.example.skillshare.ui.screens.admin

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.skillshare.ui.screens.skills.SkillDetailsScreen
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
import com.example.skillshare.ui.screens.admin.AdminDashboardScreen
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
    object SkillDetails : Screen("skill_details/{skillId}", "Skill Details") {
        fun createRoute(skillId: String) = "skill_details/$skillId"
    }
    object TrainerProfile : Screen("trainer_profile", "Trainer")
    object Payment : Screen("payment", "Payment")
    object TrainerDashboard : Screen("trainer_dashboard/{trainerId}", "Trainer Dashboard") {
        fun createRoute(trainerId: String) = "trainer_dashboard/$trainerId"
    }
    object UserDashboard : Screen("user_dashboard", "User Dashboard")
    object Profile : Screen("profile", "Profile")
    object TrainerSkills : Screen("trainer_skills/{trainerId}", "Trainer Skills") {
        fun createRoute(trainerId: String) = "trainer_skills/$trainerId"
    }
    object Chat : Screen("chat/{conversationId}", "Chat") {
        fun createRoute(conversationId: String) = "chat/$conversationId"
    }
    object Conversations : Screen("conversations", "Conversations")
    object AddSkill : Screen("add_skill_screen/{trainerId}", "Add Skill") {
        fun createRoute(trainerId: String) = "add_skill_screen/$trainerId"
    }
    object AdminDashboard : Screen("admin_dashboard", "Admin Dashboard")

}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    val application = LocalContext.current.applicationContext as Application
    val skillListViewModelFactory = SkillListViewModelFactory(application)
    val skillListViewModel: SkillListViewModel = viewModel(factory = skillListViewModelFactory)

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        // Auth routes
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Signup.route) { SignupScreen(navController) }

        // Dashboards
        composable(
            route = Screen.TrainerDashboard.route,
            arguments = listOf(navArgument("trainerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val trainerId = backStackEntry.arguments?.getString("trainerId") ?: ""
            TrainerDashboard(navController = navController, trainerId = trainerId)
        }
        // composable(Screen.UserDashboard.route) { UserDashboardScreen(navController) }


        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(navController)
        }


        // Main App Pages for Users
        composable(Screen.Search.route) {
            SearchScreen(
                navController = navController,
                viewModel = skillListViewModel,
                onSkillClick = { skillId ->
                    navController.navigate(Screen.SkillDetails.createRoute(skillId))
                }
            )
        }
        composable(
            route = Screen.SkillDetails.route,
            arguments = listOf(navArgument("skillId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("skillId")?.let { skillId ->
                SkillDetailsScreen(
                    skillId = skillId,
                    viewModel = skillListViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        // Main App Pages
        composable(Screen.Reviews.route) { ReviewScreen(navController) }
        composable(Screen.Payment.route) { PaymentScreen(navController) }

        // Profile Management
        composable(Screen.TrainerProfile.route) { TrainerProfileScreen(navController) }
        composable(Screen.LearnerProfile.route) { LearnerProfileScreen(navController) }

        // Trainer-specific screens
        composable(
            route = Screen.TrainerSkills.route,
            arguments = listOf(navArgument("trainerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val trainerId = backStackEntry.arguments?.getString("trainerId") ?: ""
            TrainerSkillsScreen(
                navController = navController,
                onSkillClick = { skillId ->
                    navController.navigate(Screen.SkillDetails.createRoute(skillId))
                },
                onBack = { navController.popBackStack() },
                viewModel = skillListViewModel,
                trainerId = trainerId
            )
        }
        composable(
            route = Screen.AddSkill.route,
            arguments = listOf(navArgument("trainerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val trainerId = backStackEntry.arguments?.getString("trainerId") ?: ""
            AddSkillScreen(
                viewModel = skillListViewModel,
                onSkillAdded = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                trainerId = trainerId
            )
        }

        // Messaging
        composable(Screen.Chat.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("conversationId") ?: ""
            ChatScreen(conversationId = id, navController = navController)
        }
        composable(Screen.Conversations.route) { ConversationsScreen(navController) }
    }
}
