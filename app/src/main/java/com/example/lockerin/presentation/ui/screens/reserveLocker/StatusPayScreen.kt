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
import com.example.lockerin.presentation.ui.components.decrypt
import com.example.lockerin.presentation.ui.components.generateAesKey
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
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


    LaunchedEffect(userID) {
        cardsViewModel.setUserId(userID)
    }

    var isLoading by remember { mutableStateOf(true) }
    Log.d("Payment", "ID: $cardID")
    LaunchedEffect(cardID,paymentID) {
        cardsViewModel.getCardById(cardID)
        paymentViewModel.getPaymentByPaymentId(paymentID)
        delay(1000)
        isLoading = false
    }

    val payment by paymentViewModel.selectedPayment.collectAsState()


    val card by cardsViewModel.selectedCard.collectAsState()
    val startDate = Date()
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    Log.d("Payment", "ID: $paymentID")

    BackHandler {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
        }
    }

    if (isLoading) {
        LoadingScreen(isLoading)
    } else {
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
                                contentDescription = "Ir atras",
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
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Pago realizado con éxito",
                    modifier = Modifier
                        .size(200.dp),
                    tint = myGreenColor
                )

                Spacer(modifier = Modifier.size(32.dp))

                Text(
                    text = "Pago realizado con éxito",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(16.dp))

                InfoRow("Código de reserva", rentalID)

                InfoRow("Fecha de pago", format.format(startDate))

                InfoRow("Tarjeta", card?.cardNumber ?: "No disponible")

                InfoRow("ID pago", paymentID)

                InfoRow("Precio total", "${payment?.amount.toString()} €")

            }
        }
    }

}




