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

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route,
        modifier = modifier
    ) {
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
