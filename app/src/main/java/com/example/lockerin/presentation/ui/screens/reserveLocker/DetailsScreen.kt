package com.example.lockerin.presentation.ui.screens.reserveLocker

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.components.InfoRow
import com.example.lockerin.presentation.ui.components.LoadingScreen
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.math.RoundingMode

@Composable
fun DetailsScreen(
    userID: String,
    lockerID: String,
    startDate: String,
    endDate: String,
    totalPrice: String,
    navController: NavHostController = rememberNavController(),
    userViewModel: UsersViewModel = koinViewModel(),
    authViewModel: AuthViewModel = viewModel(),
) {
    val userId = authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()
    Log.d("DetailsScreen", totalPrice.toString())
    val lockersViewModel: LockersViewModel = koinViewModel()
    val locker by lockersViewModel.selectedLocker.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = false
    }
    LaunchedEffect(lockerID) {
        lockersViewModel.getLockerById(lockerID)
    }

    val priceToDouble = totalPrice.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()



    if (isLoading) {
        LoadingScreen(isLoading = true)
    } else {
        DrawerMenu(
            textoBar = "Datos de la Reserva",
            navController = navController,
            authViewModel = viewModel(),
            fullUser = userState,
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {


                    locker?.let {
                        // Fecha de inicio
                        InfoRow("Fecha de inicio", startDate)

                        // Fecha de fin
                        InfoRow("Fecha de fin", endDate)

                        // Ubicación
                        InfoRow("Ubicación", it.location)


                        // Dimensiones
                        InfoRow("Dimensiones", it.dimension)


                        // Precio total
                        InfoRow("Precio total", "$priceToDouble €")

                        Spacer(modifier = Modifier.height(16.dp))

                        Row {
                            Button(
                                onClick = {
                                    navController.navigate(
                                        Screen.Payment.createRoute(
                                            userID,
                                            lockerID,
                                            startDate,
                                            endDate,
                                            priceToDouble.toString()
                                        )
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Primary)
                            ) {
                                Text(
                                    text = "Reservar",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = {
                                    navController.popBackStack()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Primary)
                            ) {
                                Text(
                                    text = "Cancelar",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    } ?: run {
                        Text(text = "No se encontró el locker con ID: $lockerID")
                    }
                }
            }
        )
    }

}

