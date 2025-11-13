package com.example.skillshare.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.skillshare.ui.screens.booking.BookingScreen
import com.example.skillshare.ui.screens.details.TrainerDetailScreen
import com.example.skillshare.ui.screens.mybookings.MyBookingsScreen
import com.example.skillshare.ui.screens.payment.PaymentScreen
import com.example.skillshare.ui.screens.profiles.LearnerProfileScreen
import com.example.skillshare.ui.screens.reviews.ReviewScreen
import com.example.skillshare.ui.screens.trainer.SearchScreen
import com.example.skillshare.ui.screens.auth.LoginScreen
import com.example.skillshare.ui.screens.auth.SignupScreen
import com.example.skillshare.ui.screens.trainer.TrainerDashboardScreen
import com.example.skillshare.ui.screens.user.UserDashboardScreen
import com.example.skillshare.ui.screens.profiles.ProfileScreen
import com.example.skillshare.ui.screens.trainer.TrainerSkillsScreen
import com.example.skillshare.ui.screens.trainer.ManageBookingsScreen
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
    object MyBookings : Screen("my_bookings", "Bookings")
    object Reviews : Screen("reviews", "Reviews")
    object LearnerProfile : Screen("learner_profile", "Profile")
    object Details : Screen("details", "Details")
    object Booking : Screen("booking", "Booking")
    object TrainerProfile : Screen("trainer_profile", "Trainer")
    object Payment : Screen("payment", "Payment")

    object TrainerDashboard : Screen("trainer_dashboard", "Trainer Dashboard")
    object UserDashboard : Screen("user_dashboard", "User Dashboard")
    object Profile : Screen("profile", "Profile")
    object TrainerSkills : Screen("trainer_skills", "Trainer Skills")
    object ManageBookings : Screen("manage_bookings", "Manage Bookings")
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
        composable(Screen.Booking.route) { BookingScreen(navController) }
        composable(Screen.MyBookings.route) { MyBookingsScreen(navController) }
        composable(Screen.Reviews.route) { ReviewScreen(navController) }
        composable(Screen.LearnerProfile.route) { LearnerProfileScreen(navController) }
        composable(Screen.TrainerProfile.route) { TrainerProfileScreen(navController) }
        composable(Screen.Payment.route) { PaymentScreen(navController) }

        //  Profile Management
        composable(Screen.Profile.route) { ProfileScreen(navController) }

        //  Trainer Screens
        composable(Screen.TrainerSkills.route) { TrainerSkillsScreen(navController) }
        composable(Screen.ManageBookings.route) { ManageBookingsScreen(navController) }

        //  Messaging
        composable("chat/{conversationId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("conversationId") ?: ""
            ChatScreen(conversationId = id, navController = navController)
        }
        composable(Screen.Conversations.route) { ConversationsScreen(navController) }
    }
}


@Composable
fun ProfileScreen(navController: NavHostController) {
    Text("Profile Management Screen")
}

@Composable
fun TrainerSkillsScreen(navController: NavHostController) {
    Text("Trainer Skills Screen")
}

@Composable
fun ManageBookingsScreen(navController: NavHostController) {
    Text("Manage Bookings Screen")
}

@Composable
fun ChatScreen(conversationId: String, navController: NavHostController) {
    Text("Chat Screen for conversation $conversationId")
}

@Composable
fun ConversationsScreen(navController: NavHostController) {
    Text("Conversations List Screen")
}

@Composable
fun TrainerProfileScreen(navController: NavHostController) {
    Text("Trainer Profile Screen")
}
