package com.example.lockerin

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.presentation.navigation.NavGraph
import com.example.lockerin.presentation.navigation.Screen
import androidx.compose.runtime.getValue
import com.example.lockerin.presentation.ui.theme.LockerinTheme
import com.example.lockerin.presentation.viewmodel.users.AuthState
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O) // Asegúrate de que esto es necesario para algo que usas
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Para mostrar el contenido detrás de las barras del sistema

        setContent {
            LockerinTheme { // Aplica tu tema
                LockerInApp() // Llama a nuestro Composable principal que gestiona la navegación y auth
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LockerInApp(
    authViewModel: AuthViewModel = viewModel()
) {

    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavGraph(
            navController = navController,
            authViewModel = authViewModel
        )
    }
}

