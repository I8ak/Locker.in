package com.example.lockerin.presentation.ui.screens

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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.myGreenColor
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.payment.PaymentViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusPayScreen(
    cardID: String,
    paymentID: String,
    navController: NavHostController = rememberNavController(),
) {

    val graphViewModelStoreOwner = remember(navController.graph.id) {
        navController.getViewModelStoreOwner(navController.graph.id)
    }

    val paymentViewModel: PaymentViewModel = viewModel(viewModelStoreOwner = graphViewModelStoreOwner)
    val cardsViewModel: CardsViewModel = viewModel(viewModelStoreOwner = graphViewModelStoreOwner)
    val startDate = Date()
    val  format = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
    Log.d("Payment", "ID: $paymentID")
    val payment = paymentViewModel.getPaymentByPaymentId(paymentID)
    Log.d("Payment", "Payment: $payment")
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
                        onClick = {  navController.popBackStack() }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBack
                            ,contentDescription = "Ir atras"
                            ,tint = Color.Black,
                            modifier = Modifier.size(30.dp))
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
                    .size(200.dp)
                ,
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
                    text = "Fecha de inicio",
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
            val card = cardsViewModel.getCardById(cardID)
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
                    text = cardsViewModel.hasNumberCard(card?.cardNumber ?: String()),
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                        .weight(1f)
                        .height(50.dp) ,
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
                Text(
                    text = payment?.paymentID ?: String(),
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

@Preview(showBackground = true)
@Composable
fun StatusPayScreenPreview() {
    StatusPayScreen(cardID = "1", paymentID = "1")
}