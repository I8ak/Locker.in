package com.example.lockerin

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import android.Manifest
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.lockerin.data.utils.RentalReminderWorker
import com.example.lockerin.presentation.navigation.NavGraph

import com.example.lockerin.presentation.ui.theme.LockerinTheme
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val reminderWorkRequest = PeriodicWorkRequestBuilder<RentalReminderWorker>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "rental_reminder_work",
            ExistingPeriodicWorkPolicy.KEEP,
            reminderWorkRequest
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.Black.toArgb()

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = false



        setContent {
            LockerinTheme {
                val navController = rememberNavController()
                val authViewModel = viewModel<AuthViewModel>()

                val startDestinationFromNotification = intent?.getStringExtra("navigate_to")

                NavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    startDestinationFromNotification = startDestinationFromNotification
                )
            }
        }
    }
}


