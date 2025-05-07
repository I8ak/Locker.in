package com.example.lockerin.presentation.ui.screens.reserveLocker

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lockerin.R
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.domain.model.Payment
import com.example.lockerin.domain.model.Rental
import com.example.lockerin.domain.model.User
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import com.example.lockerin.domain.model.HistoricRental
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.lockers.RentalViewModel
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.payment.HistoricalRentalViewModel
import com.example.lockerin.presentation.viewmodel.payment.PaymentViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservedLockersScreen(
    userID: String,
    navController: NavHostController,
    lockersViewModel: LockersViewModel = koinViewModel(),
    rentalViewModel: RentalViewModel = koinViewModel(),
    paymentViewModel: PaymentViewModel = koinViewModel(),
    usersViewModel: UsersViewModel = koinViewModel(),
    historicalRentalViewModel: HistoricalRentalViewModel = koinViewModel(),
) {



    val userState by usersViewModel.user.collectAsState()

    val rentalState by rentalViewModel.rentals.collectAsState()
    val lockers by lockersViewModel.lockers.collectAsState()
    val payments by paymentViewModel.payments.collectAsState()
    val historicRentalState by historicalRentalViewModel.historicalRental.collectAsState()

    LaunchedEffect(userID) {
        rentalViewModel.setUserId(userID)
        paymentViewModel.setUserId(userID)
        historicalRentalViewModel.setUserId(userID)
    }


    LaunchedEffect(userID) {
        rentalViewModel.getRentalByUserId(userID)
    }





    DrawerMenu(
        textoBar = "Reservas",
        navController = navController,
        authViewModel = viewModel(),
        fullUser = userState,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Reservas activas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                LazyColumn {
                    items(rentalState) { rentalLazy ->
                        val lockerMatch = lockers.find { it.lockerID == rentalLazy.lockerID }
                        val paymentMatch = payments.find { it.rentalID == rentalLazy.rentalID }

                        Log.d("locker", lockerMatch.toString())
                        Log.d("rental", rentalLazy.toString())
                        Log.d("payment", paymentMatch.toString())

                        key(rentalLazy.lockerID) {
                            Spacer(modifier = Modifier.size(8.dp))
                            CardReserved(
                                locker = lockerMatch,
                                rental = rentalLazy,
                                payment = paymentMatch
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    text = "Historial de reservas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                LazyColumn {
                    items(
                        historicRentalState
                    ) { historicLazy ->
                        key(historicLazy.historicID) {
                            Spacer(modifier = Modifier.size(8.dp))
                            CardHistoricRents(
                                historicLazy
                            )
                        }
                    }
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardReserved(
    locker: Locker?,
    rental: Rental?,
    payment: Payment?,
    historicalRentalViewModel: HistoricalRentalViewModel = koinViewModel(),
    lockersViewModel: LockersViewModel = koinViewModel(),
    rentalViewModel: RentalViewModel = koinViewModel(),
    cardsViewModel: CardsViewModel = koinViewModel(),
) {


    var isSelected by remember { mutableStateOf(false) }
    val imagen = when (locker?.size) {
        "Small" -> R.drawable.personal_bag
        "Medium" -> R.drawable.luggage
        else -> R.drawable.trolley
    }

    LaunchedEffect(payment?.cardID) {
        cardsViewModel.getCardById(payment?.cardID.toString())
    }
    val card by cardsViewModel.selectedCard.collectAsState()


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .clickable {
                isSelected = !isSelected
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .background(BeigeClaro)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imagen),
                    contentDescription = "imagen ${locker?.size}",
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = rental?.rentalID?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Hasta ${convertDateToString(rental?.endDate)}",
                        modifier = Modifier.padding(top = 8.dp), color = Color.Black
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Localización: ${locker?.location},${locker?.city} ", color = Color.Black)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Precio: ${payment?.amount}€", color = Color.Black)
                        Spacer(modifier = Modifier.size(8.dp))
                        var finished=CountDown(rental?.endDate)
                        if (finished == "Finalizado") {
                            val historicRentalID = FirebaseFirestore.getInstance().collection("historicRentals").document().id
                            if (rental != null) {
                                historicalRentalViewModel.addHistoricRental(
                                    HistoricRental(
                                        historicID = historicRentalID,
                                        userID = rental.userID,
                                        location = locker?.location.toString(),
                                        city = locker?.city.toString(),
                                        size = locker?.size.toString(),
                                        dimension = locker?.dimension.toString(),
                                        cardNumber = card?.cardNumber.toString(),
                                        typeCard = card?.typeCard.toString(),
                                        amount = payment?.amount ?: 0.0,
                                        status = true,
                                        startDate = rental.startDate,
                                        endDate = rental.endDate
                                    )
                                )
                                lockersViewModel.setStatus(locker?.lockerID.toString(), false)
                                rentalViewModel.deleteRental(rental)
                            }



                        }

                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun convertDateToString(endDate: Date?): String {
    val instant: Instant = endDate?.toInstant() ?: Date().toInstant()
    val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return localDateTime.format(formatter)
}

@Composable
fun CountDown(endDate: Date?): String {
    var timeleftString by remember { mutableStateOf("Calculando...") }
    LaunchedEffect(endDate) {
        if (endDate == null) {
            timeleftString = "No hay fecha"
            return@LaunchedEffect
        }
        val endTime = endDate.time
        while (true) {
            val timeMilis = System.currentTimeMillis()
            val timeleft = endTime - timeMilis
            if (timeleft <= 0) {
                timeleftString = "Finalizado"
                break
            } else {
                val totalSeconds = timeleft / 1000
                val seconds = totalSeconds % 60
                val totalMinutes = totalSeconds / 60
                val minutes = totalMinutes % 60
                val totalHours = totalMinutes / 60
                val hours = totalHours % 24
                val days = totalHours / 24

                timeleftString = buildString {
                    if (days > 0) append("${days}d ")
                    if (hours > 0 || days > 0) append("${hours}h ")
                    if (minutes > 0 || hours > 0 || days > 0) append("${minutes}m ")
                    append("${seconds}s")
                }.trim()

                if (timeleftString.isBlank()) {
                    timeleftString = "0s"
                }

                delay(1000)
            }
        }
    }
    Text(text = "Finaliza en $timeleftString", color = Color.Black, fontSize = 20.sp)
    return timeleftString
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardHistoricRents(
    historicRental: HistoricRental?
) {
    var isSelected by remember { mutableStateOf(false) }

    val cardsViewModel: CardsViewModel = koinViewModel()


    var textStatus: String
    var colorStatus: Color

    val arrowIcon = if (isSelected) {
        Icons.Default.KeyboardDoubleArrowUp
    } else {
        Icons.Default.KeyboardDoubleArrowDown
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .clickable {
                isSelected = !isSelected
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {

        Column(
            modifier = Modifier
                .background(BeigeClaro)
                .padding(12.dp)
        ) {

            if (historicRental?.status == true) {
                textStatus = "Pagado"
                colorStatus = Color.Green

            } else {
                textStatus = "Pendiente"
                colorStatus = Color.Red
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = textStatus, color = colorStatus, fontSize = 20.sp)
                Icon(
                    imageVector = arrowIcon,
                    contentDescription = "Ayuda",
                    tint = Color.Black,
                    modifier = Modifier
                        .clickable {
                            isSelected = !isSelected
                        }
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = "Precio total: ${historicRental?.amount}€", color = Color.Black)
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Fecha de reserva: ${convertDateToString(historicRental?.createdAt)}",
                color = Color.Black
            )
            if (isSelected) {
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = "Localización: ${historicRental?.location} ${historicRental?.city}",
                    color = Color.Black
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = "Fecha de inicio: ${convertDateToString(historicRental?.startDate)}",
                    color = Color.Black
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = "Fecha de fin: ${convertDateToString(historicRental?.endDate)}",
                    color = Color.Black
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = "Tipo de casillero: ${historicRental?.size} ${historicRental?.dimension}",
                    color = Color.Black
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = "Número de tarjeta: ${cardsViewModel.hasNumberCard(historicRental?.cardNumber.toString())}",
                    color = Color.Black
                )

            }
        }


    }

}
