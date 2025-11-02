package com.example.skillshare.navigation

sealed class Screen(val route: String, val title: String) {
    object Search : Screen("search", "Discover")
    object Details : Screen("details", "Details")
    object Booking : Screen("booking", "Book")
    object MyBookings : Screen("my_bookings", "My Bookings")
    object Reviews : Screen("reviews", "Reviews")
    object LearnerProfile : Screen("learner_profile", "My Profile")
    object TrainerProfile : Screen("trainer_profile", "Trainer Profile")
}
