package com.example.lockerin

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.presentation.navigation.NavGraph
import com.example.lockerin.presentation.navigation.Screen
import androidx.compose.runtime.getValue
import com.example.lockerin.presentation.ui.components.LoadingScreen
import com.example.lockerin.presentation.ui.theme.LockerinTheme
import com.example.lockerin.presentation.viewmodel.AppViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthState
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel



class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LockerinTheme {
                LockerInApp()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LockerInApp(
    authViewModel: AuthViewModel = viewModel(),
    appViewModel: AppViewModel = viewModel()
) {
    val navController = rememberNavController()

    val isLoading by appViewModel.isLoading

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavGraph(
                navController = navController,
                authViewModel = authViewModel,
                appViewModel = appViewModel
            )
            LoadingScreen(isLoading = isLoading)
        }
    }
}

