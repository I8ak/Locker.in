package com.example.lockerin.presentation.navigation

import MapScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
import com.example.lockerin.presentation.ui.components.SplashScreen
import com.example.lockerin.presentation.ui.screens.locker.EditLockerScreen
import com.example.lockerin.presentation.ui.screens.reserveLocker.StatusPayScreen
import com.example.lockerin.presentation.ui.screens.user.AccountScreen
import com.example.lockerin.presentation.ui.screens.user.ChooseAvatarScreen
import com.example.lockerin.presentation.ui.screens.user.ConfigurationScreen
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route,
    authViewModel: AuthViewModel,
    startDestinationFromNotification: String? = null
) {
    LaunchedEffect(startDestinationFromNotification) {
        if (startDestinationFromNotification == "active_rental") {
            val userId = authViewModel.currentUserId
            if (userId != null) {
                navController.navigate(Screen.ResrvedLockers.createRoute(userId))
            } else {
                navController.navigate(Screen.Login.route)
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController,authViewModel)
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                navController,
                authViewModel = authViewModel
            )
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
                navArgument("userID") { type = NavType.StringType },
                navArgument("cardID") { type = NavType.StringType },
                navArgument("paymentID") { type = NavType.StringType },
                navArgument("rentalID") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            val cardID = backStackEntry.arguments?.getString("cardID")!!
            val paymentID = backStackEntry.arguments?.getString("paymentID")!!
            val rentalID = backStackEntry.arguments?.getString("rentalID")!!
            StatusPayScreen( userID,cardID,paymentID,rentalID, navController = navController)
        }
        composable(
            route = Screen.Account.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            AccountScreen(userID, navController)
        }
        composable(
            route = Screen.Information.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            ConfigurationScreen(userID, navController)
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
            route = Screen.ResrvedLockers.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            ReservedLockersScreen( userID,
                navController = navController
            )
        }
        composable(Screen.EmailResetPass.route) {
            EmailResetPassScreen(navController)
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
        composable(Screen.MapScreen.route) {
            MapScreen(navController)
        }
        composable(
            route = Screen.ChooseAvatar.route,
            arguments = listOf(
                navArgument("userID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")!!
            ChooseAvatarScreen(userID, navController)
        }
    }
}
