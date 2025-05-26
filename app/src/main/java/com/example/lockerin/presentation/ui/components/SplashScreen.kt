package com.example.lockerin.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.R
import com.example.lockerin.data.utils.NetworkUtils
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showNoConnectionDialog by remember { mutableStateOf(false) }

    // Este LaunchedEffect se ejecuta una vez cuando el Composable entra en la composición
    LaunchedEffect(Unit) {
        delay(1000)

        fun checkAndNavigateLogic() {
            if (NetworkUtils.isInternetAvailable(context)) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                } else {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            } else {
                showNoConnectionDialog = true
            }
        }

        checkAndNavigateLogic()
    }

    // Diseño de la pantalla de bienvenida (logo, etc.)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BeigeClaro),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.lockerlogo),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    // Diálogo para cuando no hay conexión a internet
    if (showNoConnectionDialog) {
        AlertDialog(
            onDismissRequest = {},
            containerColor = BeigeClaro,
            title = {
                Text(
                    "Sin conexión a Internet",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Por favor, comprueba tu conexión a Internet e inténtalo de nuevo.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    color = Color.Black,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showNoConnectionDialog = false
                        scope.launch {
                            delay(500)
                            fun checkAndNavigateOnRetry() {
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    val currentUser = FirebaseAuth.getInstance().currentUser
                                    if (currentUser != null) {
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Splash.route) {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        navController.navigate(Screen.Login.route) {
                                            popUpTo(Screen.Splash.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                } else {
                                    showNoConnectionDialog = true
                                }
                            }
                            checkAndNavigateOnRetry()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Reintentar",color = White)
                }
            }
        )
    }
}
