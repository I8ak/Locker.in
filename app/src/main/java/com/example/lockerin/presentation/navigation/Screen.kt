package com.example.lockerin.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Reserve : Screen("lockers/reserve/{city}") {
        fun createRoute(city: String): String = "lockers/reserve/$city"
    }
    object Details : Screen("lockers/details/{lockerID}/{startDate}/{endDate}/{totalPrice}") {
        fun createRoute(lockerID: String, startDate: String, endDate: String, totalPrice: String): String =
            "lockers/details/$lockerID/$startDate/$endDate/$totalPrice"
    }
    object Payment : Screen("lockers/payment/{userID}/{lockerID}/{startDate}/{endDate}/{totalPrice}") {
        fun createRoute(userID: String, lockerID: String, startDate: String, endDate: String, totalPrice: String): String =
            "lockers/payment/$userID/$lockerID/$startDate/$endDate/$totalPrice"
    }
    object StatusPay : Screen("lockers/statuaPay/{cardID}/{paymentID}") {
        fun createRoute(cardId: String, paymentID: String): String =
            "lockers/statuaPay/$cardId/$paymentID"
    }


}