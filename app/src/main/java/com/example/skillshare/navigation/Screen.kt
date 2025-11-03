package com.example.skillshare.navigation

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
}
