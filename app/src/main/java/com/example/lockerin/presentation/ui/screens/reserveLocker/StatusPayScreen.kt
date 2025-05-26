package com.example.lockerin.presentation.ui.screens.reserveLocker

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.myGreenColor
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.payment.PaymentViewModel
import java.text.SimpleDateFormat
import java.util.Date
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.InfoRow
import com.example.lockerin.presentation.ui.components.LoadingScreen
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusPayScreen(
    userID: String,
    cardID: String,
    paymentID: String,
    rentalID: String,
    paymentViewModel: PaymentViewModel = koinViewModel(),
    cardsViewModel: CardsViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    // Establece el ID de usuario en el CardsViewModel cuando la pantalla se lanza.
    LaunchedEffect(userID) {
        cardsViewModel.setUserId(userID)
    }

    // Estados para controlar la carga y el temporizador.
    var isLoading by remember { mutableStateOf(true) }
    var secondsRemaining by remember { mutableIntStateOf(10) }

    // Registra el ID de la tarjeta para depuración.
    Log.d("Payment", "ID: $cardID")

    // Efecto para obtener los datos de la tarjeta y el pago, y simular la carga.
    LaunchedEffect(cardID, paymentID) {
        cardsViewModel.getCardById(cardID)
        paymentViewModel.getPaymentByPaymentId(paymentID)
        delay(1000) // Simula un tiempo de carga de 1 segundo.
        isLoading = false
    }

    // Efecto para el temporizador de cuenta regresiva y navegación automática.
    LaunchedEffect(key1 = "countdown") {
        while (secondsRemaining > 0) {
            delay(1000) // Espera 1 segundo.
            secondsRemaining--
        }
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
        }
    }

    // Recopila el estado del pago y la tarjeta del ViewModel.
    val payment by paymentViewModel.selectedPayment.collectAsState()
    val card by cardsViewModel.selectedCard.collectAsState()

    // Configuración inicial de la interfaz de usuario basada en el estado del pago.
    val startDate = Date()
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var icon = Icons.Default.CheckCircle
    var color = myGreenColor
    var textMessage = "Pago realizado con éxito"

    // Maneja el botón de retroceso para navegar a la pantalla de inicio.
    BackHandler {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
        }
    }

    // Muestra la pantalla de carga si isLoading es verdadero.
    if (isLoading) {
        LoadingScreen(isLoading)
    } else {
        // Ajusta el icono, color y mensaje si el pago fue rechazado.
        if (payment?.status == false) {
            icon = Icons.Default.RemoveCircle
            color = Color.Red
            textMessage = "Pago rechazado"
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BeigeClaro,
                        titleContentColor = Color.Black,
                        navigationIconContentColor = Color.Black
                    ),
                    title = {
                        Text(
                            text = "Pago",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.navigate(Screen.Home.route) }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Ir atrás",
                                tint = Color.Black,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    },
                    actions = {
                        Spacer(modifier = Modifier.width(48.dp))
                    },
                )
            },
            containerColor = BeigeClaro
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icono de estado de pago
                Icon(
                    imageVector = icon,
                    contentDescription = "Estado del pago",
                    modifier = Modifier.size(200.dp),
                    tint = color
                )

                Spacer(modifier = Modifier.size(32.dp))

                // Mensaje de estado de pago
                Text(
                    text = textMessage,
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(16.dp))

                // Mensaje de cuenta regresiva
                Text(
                    text = "Volviendo al inicio en $secondsRemaining segundos...",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.size(16.dp))

                // Detalles del pago si fue exitoso
                if (payment?.status == true) {
                    InfoRow(label = "Código de reserva", value = rentalID)
                    InfoRow(label = "Fecha de pago", value = format.format(startDate))
                    InfoRow(label = "Tarjeta", value = card?.cardNumber ?: "No disponible")
                    InfoRow(label = "ID pago", value = paymentID)
                    InfoRow(label = "Precio total", value = "${payment?.amount.toString()} €")
                }
            }
        }
    }
}