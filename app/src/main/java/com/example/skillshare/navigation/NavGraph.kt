package com.example.skillshare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.skillshare.ui.screens.*
import com.example.skillshare.ui.screens.booking.BookingScreen
import com.example.skillshare.ui.screens.details.TrainerDetailScreen
import com.example.skillshare.ui.screens.mybookings.MyBookingsScreen
import com.example.skillshare.ui.screens.payment.PaymentScreen
import com.example.skillshare.ui.screens.profiles.LearnerProfileScreen
import com.example.skillshare.ui.screens.profiles.TrainerProfileScreen
import com.example.skillshare.ui.screens.reviews.ReviewScreen
import com.example.skillshare.ui.screens.search.SearchScreen
import com.example.skillshare.ui.screens.auth.LoginScreen
import com.example.skillshare.ui.screens.auth.SignupScreen


@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route, // ⬅ start from login screen
        modifier = modifier
    ) {
        // Auth routes
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Signup.route) { SignupScreen(navController) }

        // Main app routes
        composable(Screen.Search.route) { SearchScreen(navController) }
        composable(Screen.Details.route) { TrainerDetailScreen(navController) }
        composable(Screen.Booking.route) { BookingScreen(navController) }
        composable(Screen.MyBookings.route) { MyBookingsScreen(navController) }
        composable(Screen.Reviews.route) { ReviewScreen(navController) }
        composable(Screen.LearnerProfile.route) { LearnerProfileScreen(navController) }
        composable(Screen.TrainerProfile.route) { TrainerProfileScreen(navController) }
        composable(Screen.Payment.route) { PaymentScreen(navController) }
    }
}
