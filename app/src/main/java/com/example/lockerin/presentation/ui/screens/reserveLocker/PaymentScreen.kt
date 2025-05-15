package com.example.lockerin.presentation.ui.screens.reserveLocker

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.R
import com.example.lockerin.domain.model.Tarjeta
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lockerin.domain.model.Payment
import com.example.lockerin.domain.model.Rental
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.components.LoadingScreen
import com.example.lockerin.presentation.ui.components.decrypt
import com.example.lockerin.presentation.ui.components.generateAesKey
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.lockers.RentalViewModel
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.payment.PaymentViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentScreen(
    userID: String,
    lockerID: String,
    startDate: String,
    endDate: String,
    totalPrice: String,
    navController: NavHostController = rememberNavController(),
    userViewModel: UsersViewModel= koinViewModel(),
    lockersViewModel: LockersViewModel= koinViewModel(),
    paymentViewModel: PaymentViewModel = koinViewModel(),
    cardsViewModel: CardsViewModel = koinViewModel(),
    rentalViewModel: RentalViewModel = koinViewModel(),
    authViewModel: AuthViewModel=viewModel(),
    ) {


    val userId = authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()
    val user=userViewModel.getUserById(userId.toString())
    val cardsState by cardsViewModel.cards.collectAsState()
    LaunchedEffect(userID) {
        cardsViewModel.setUserId(userID)
    }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = false
    }
    if (isLoading) {
        LoadingScreen(isLoading)
    } else {
        DrawerMenu(
            textoBar = "Medios de pago",
            navController = navController,
            authViewModel = viewModel(),
            fullUser = userState,
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    var selectedCardId by remember { mutableStateOf<String?>(null) }
                    Text(
                        text = "Selecciona una tarjeta",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.padding(16.dp))

                    LazyColumn(
                        modifier = Modifier
                            .background(Color.Transparent)
                    ) {
                        items(
                            cardsState
                        ) { card ->
                            key(card.cardID) {
                                Log.d("Card", "Tarjeta: ${card.cardID}")
                                CardsCard(
                                    tarjeta = card,
                                    isSelected = card.cardID == selectedCardId,
                                    onCardSelected = { selectedCardId = it },
                                    cardsViewModel = cardsViewModel,

                                    )
                            }

                        }
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "Añadir una tarjeta nueva",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.AddCard,
                            contentDescription = "AddCard",
                            tint = Color.Black,
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.AddCard.createRoute(userID))
                            }
                        )
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Precio total",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp)
                                .weight(1f)
                                .height(30.dp),
                            fontSize = 20.sp,
                            color = Color.Black,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$totalPrice €",
                            modifier = Modifier
                                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp)
                                .weight(1f)
                                .height(30.dp),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }




                    Spacer(modifier = Modifier.padding(16.dp))
                    Row {
                        Button(
                            enabled = selectedCardId != null,
                            onClick = {
                                val rentalIDRandom = generarNumeroSeisDigitos().toString()
                                val currebntCardId=selectedCardId
                                val rental= Rental(
                                    rentalID = rentalIDRandom,
                                    userID = userID,
                                    lockerID = lockerID,
                                    startDate = transformDate(startDate),
                                    endDate = transformDate(endDate),
                                )
                                val paymentID = FirebaseFirestore.getInstance().collection("payments").document().id
                                val payment=Payment(
                                    paymentID = paymentID,
                                    userID = userID,
                                    rentalID = rental.rentalID,
                                    cardID = currebntCardId.toString(),
                                    amount = totalPrice.toDouble(),
                                    status = true,
                                )
                                lockersViewModel.setStatus(lockerID,false)
                                rentalViewModel.addRental(
                                    rental
                                )
                                paymentViewModel.addPayment(payment)
                                Log.d("Rental", "Alquiler agregado: $rental")
                                Log.d("Payment", "Pago agregado: $payment")
                                navController.navigate(
                                    Screen.StatusPay.createRoute(
                                        userID,
                                        currebntCardId.toString(),
                                        payment.paymentID,rentalIDRandom)){
                                    popUpTo(Screen.Payment.route) {
                                        inclusive = true
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary,
                                disabledContainerColor = Color.Gray),
                            modifier = Modifier.width(130.dp)
                        ) {
                            Text(text = "Pagar", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                navController.navigate(Screen.Home.route)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            modifier = Modifier.width(130.dp)
                        ) {
                            Text(text = "Cancelar", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        )

    }
}

fun generarNumeroSeisDigitos(): Int {
    return Random.nextInt(100000, 1000000)
}

@RequiresApi(Build.VERSION_CODES.O)
fun transformDate(date: String): Date {
    val formatter= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    val localDateTime = LocalDateTime.parse(date, formatter)
    val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
    return Date.from(instant)

}
@Composable
fun CardsCard(
    tarjeta: Tarjeta,
    isSelected: Boolean,
    onCardSelected: (cardId: String) -> Unit,
    cardsViewModel: CardsViewModel
) {
    val context = LocalContext.current
    val key = remember { generateAesKey(context) }
    val imagen = when (tarjeta.typeCard) {
        "Visa" -> R.drawable.visa
        "MasterCard" -> R.drawable.mastercard
        "American Express" -> R.drawable.american_express
        else -> R.drawable.credit_card
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = tarjeta.typeCard,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = imagen),
                    contentDescription = tarjeta.typeCard,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(60.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = tarjeta.cardNumber, color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                RadioButton(
                    selected = isSelected,
                    onClick = { onCardSelected(tarjeta.cardID) },
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black
                    )
                )
            }
        }
    }
}

