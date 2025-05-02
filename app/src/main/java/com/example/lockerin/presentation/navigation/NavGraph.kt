package com.example.lockerin.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.lockerin.presentation.ui.screens.user.AcountScreen
import com.example.lockerin.presentation.ui.screens.card.AddCardScreen
import com.example.lockerin.presentation.ui.screens.locker.AddLockerScreen
import com.example.lockerin.presentation.ui.screens.card.CardsScreen
import com.example.lockerin.presentation.ui.screens.reserveLocker.DetailsScreen
import com.example.lockerin.presentation.ui.screens.user.EmailResetPassScreen
import com.example.lockerin.presentation.ui.screens.HomeScreen
import com.example.lockerin.presentation.ui.screens.locker.ListLockersScreen
import com.example.lockerin.presentation.ui.screens.user.LoginScreen
import com.example.lockerin.presentation.ui.screens.reserveLocker.PaymentScreen
import com.example.lockerin.presentation.ui.screens.user.RegisterScreen
import com.example.lockerin.presentation.ui.screens.reserveLocker.ReserveScreen
import com.example.lockerin.presentation.ui.screens.reserveLocker.ReservedLockersScreen
import com.example.lockerin.presentation.ui.screens.user.ResetPass
import com.example.lockerin.presentation.ui.screens.SplashScreen
import com.example.lockerin.presentation.ui.screens.locker.EditLockerScreen
import com.example.lockerin.presentation.ui.screens.reserveLocker.StatusPayScreen
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route,
    authViewModel: AuthViewModel
) {
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
            RegisterScreen(
                navController,
                authViewModel = authViewModel)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(
            route = Screen.Reserve.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType },
                navArgument("city") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            val city = backStackEntry.arguments?.getString("city")!!
            ReserveScreen(userID, navController, city)
        }
        composable(
            route = Screen.Details.route,
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
            DetailsScreen(userID, lockerID, startDate, endDate,totalPrice,
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
//        composable(Screen.Configuration.route) {
//            ConfigurationScreen(navController)
//        }
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
            route = Screen.ResrvedLockers.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType },
                navArgument("lockerID") { type = NavType.StringType },
                navArgument("rentalID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            val lockerID = backStackEntry.arguments?.getString("lockerID")!!
            val rentalID = backStackEntry.arguments?.getString("rentalID")!!
            ReservedLockersScreen( userID, lockerID, rentalID,
                navController = navController
            )
        }
        composable(Screen.EmailResetPass.route) {
            EmailResetPassScreen(navController)
        }
        composable(Screen.ResetPass.route) {
            ResetPass(navController)
        }

        composable(
            route = Screen.ListLockers.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            ListLockersScreen(userID,navController)
        }
        composable(
            route = Screen.AddLocker.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            AddLockerScreen(userID,navController)
        }
        composable(
            route = Screen.EditLocker.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType },
                navArgument("lockerID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            val lockerID = backStackEntry.arguments?.getString("lockerID")!!
            EditLockerScreen(userID,lockerID,navController )
        }
    }
}
