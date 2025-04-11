package com.example.lockerin.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Reserve : Screen("lockers/reserve/{city}") {
        fun createRoute(city: String): String = "lockers/reserve/$city"
    }
}