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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.lockerin.presentation.ui.components.decrypt
import com.example.lockerin.presentation.ui.components.generateAesKey
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
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
    authViewModel: AuthViewModel = koinViewModel(),
    userViewModel: UsersViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController(),
) {
//    val userId = authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()

    val user by userViewModel.user.collectAsState()
    val key= generateAesKey()
    LaunchedEffect(userID) {
        cardsViewModel.setUserId(userID)
    }

    Log.d("Payment", "ID: $cardID")
    LaunchedEffect(paymentID) {
        paymentViewModel.getPaymentByPaymentId(paymentID)
    }

    val payment by paymentViewModel.selectedPayment.collectAsState()

    LaunchedEffect(cardID) {
        cardsViewModel.getCardById(cardID)
    }
    val card by cardsViewModel.selectedCard.collectAsState()
    val startDate = Date()
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    Log.d("Payment", "ID: $paymentID")
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = "Codigo del locker",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(8.dp)
                        .height(50.dp)
                        .weight(1f),
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = rentalID,
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                        .height(50.dp)
                        .weight(1f),
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = "Fecha de pago",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(8.dp)
                        .height(50.dp)
                        .weight(1f),
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = format.format(startDate),
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                        .height(50.dp)
                        .weight(1f),
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            // Fecha de fin
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = "Tarjeta",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(8.dp)
                        .height(50.dp)
                        .weight(1f),
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = decrypt(card?.cardNumber.toString(), card?.iv.toString(),key),
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                        .weight(1f)
                        .height(50.dp),
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

// Ubicación
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = "ID pago",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(8.dp)
                        .height(50.dp)
                        .weight(1f),
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Log.d("Payment", "PaymentID: $paymentID")
                Text(
                    text = paymentID,
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                        .weight(1f)
                        .height(50.dp),
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .heightIn(min = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Precio total",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(8.dp)
                        .height(50.dp)
                        .weight(1f),
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = payment?.amount.toString(),
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                        .weight(1f)
                        .height(50.dp),
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }


        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun StatusPayScreenPreview() {
//    StatusPayScreen(cardID = "1", paymentID = "1")
//}