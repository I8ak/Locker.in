package com.example.lockerin.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lockerin.presentation.ui.screens.AcountScreen
import com.example.lockerin.presentation.ui.screens.AddCardScreen
import com.example.lockerin.presentation.ui.screens.CardsScreen
import com.example.lockerin.presentation.ui.screens.ConfigurationScreen
import com.example.lockerin.presentation.ui.screens.DetailsScreen
import com.example.lockerin.presentation.ui.screens.HomeScreen
import com.example.lockerin.presentation.ui.screens.LoginScreen
import com.example.lockerin.presentation.ui.screens.PaymentScreen
import com.example.lockerin.presentation.ui.screens.RegisterScreen
import com.example.lockerin.presentation.ui.screens.ReserveScreen
import com.example.lockerin.presentation.ui.screens.ReservedLockersScreen
import com.example.lockerin.presentation.ui.screens.SplashScreen
import com.example.lockerin.presentation.ui.screens.StatusPayScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    startDestination: String = Screen.Splash.route,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(
            route = Screen.Reserve.route,
            arguments = listOf(
                navArgument("city") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city")!!
            ReserveScreen(navController, city)
        }
        composable(
            route = Screen.Details.route,
            arguments = listOf(
                navArgument("lockerID") { type = NavType.StringType },
                navArgument("startDate") { type = NavType.StringType },
                navArgument("endDate") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val lockerID = backStackEntry.arguments?.getString("lockerID")!!
            val startDate = backStackEntry.arguments?.getString("startDate")!!
            val endDate = backStackEntry.arguments?.getString("endDate")!!
            val totalPrice = backStackEntry.arguments?.getString("totalPrice")!!
            DetailsScreen( lockerID, startDate, endDate,totalPrice,
                navController = navController
            )
        }
        composable(
            route = Screen.Payment.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType },
                navArgument("lockerID") { type = NavType.StringType },
                navArgument("startDate") { type = NavType.StringType },
                navArgument("endDate") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            val lockerID = backStackEntry.arguments?.getString("lockerID")!!
            val startDate = backStackEntry.arguments?.getString("startDate")!!
            val endDate = backStackEntry.arguments?.getString("endDate")!!
            val totalPrice = backStackEntry.arguments?.getString("totalPrice")!!
            PaymentScreen( userID,lockerID, startDate, endDate,totalPrice,
                navController = navController
            )
        }
        composable(
            route = Screen.StatusPay.route,
            arguments = listOf(
                navArgument("cardID") { type = NavType.StringType },
                navArgument("paymentID") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("cardID")!!
            val lockerID = backStackEntry.arguments?.getString("paymentID")!!
            StatusPayScreen( userID,lockerID, navController = navController)
        }
        composable(
            route = Screen.Acount.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            AcountScreen(userID, navController)
        }
        composable(Screen.Configuration.route) {
            ConfigurationScreen(navController)
        }
        composable(
            route = Screen.Cards.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            CardsScreen(userID,navController)
        }
        composable(
            route = Screen.AddCard.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            AddCardScreen(userID,navController)
        }
        composable(
            route = Screen.Reserve.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType },
                navArgument("lockerID") { type = NavType.StringType },
                navArgument("rentalID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            val lockerID = backStackEntry.arguments?.getString("lockerID")!!
            val rentalID = backStackEntry.arguments?.getString("rentalID")!!
            ReservedLockersScreen( lockerID, lockerID, rentalID,
                navController = navController
            )
        }
    }
}
