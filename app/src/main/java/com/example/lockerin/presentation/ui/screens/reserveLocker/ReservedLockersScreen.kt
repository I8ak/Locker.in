package com.example.lockerin.presentation.ui.screens.reserveLocker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RatingBar
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat.startActivity
import com.example.lockerin.domain.model.HistoricRental
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.components.LoadingScreen
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.ui.theme.Secondary
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.rentals.RentalViewModel
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.rentals.HistoricalRentalViewModel
import com.example.lockerin.presentation.viewmodel.payment.PaymentViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import java.nio.file.WatchEvent
import androidx.core.net.toUri

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

    BackHandler {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
        }
    }
    val userState by usersViewModel.user.collectAsState()
    val user = usersViewModel.getUserById(userID)

    val rentalState by rentalViewModel.rentals.collectAsState()
    val lockers by lockersViewModel.lockers.collectAsState()
    val payments by paymentViewModel.payments.collectAsState()
    val historicRentalState by historicalRentalViewModel.historicalRental.collectAsState()
    val payed by historicalRentalViewModel.payedCount.collectAsState()
    val canceled by historicalRentalViewModel.canceledCount.collectAsState()

    LaunchedEffect(userID) {
        rentalViewModel.setUserId(userID)
        paymentViewModel.setUserId(userID)
        historicalRentalViewModel.setUserId(userID)
        historicalRentalViewModel.countHistoricRentals(userID)
    }

    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = false
    }

    if (isLoading) {
        LoadingScreen(isLoading = true)
    } else {
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
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
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
                    Row (
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            text = "Historial de reservas",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        )
                        IconDesplegable(historicRentalState.size,payed,canceled)
                    }


                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(
                            historicRentalState
                        ) { historicLazy ->
                            key(historicLazy.historicID) {
                                Spacer(modifier = Modifier.size(8.dp))
                                CardHistoricRents(
                                    historicLazy,
                                    lockersViewModel,
                                    historicalRentalViewModel
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardReserved(
    locker: Locker?,
    rental: Rental?,
    payment: Payment?,
    cardsViewModel: CardsViewModel = koinViewModel(),
    rentalViewModel: RentalViewModel = koinViewModel(),
) {
    var isSelected by remember { mutableStateOf(false) }
    val imagen = when (locker?.size) {
        "Small" -> R.drawable.personal_bag
        "Medium" -> R.drawable.luggage
        else -> R.drawable.trolley
    }

    val context = LocalContext.current

    LaunchedEffect(payment?.cardID) {
        cardsViewModel.getCardById(payment?.cardID.toString())
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
                        text = rental?.rentalID ?: "",
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
                        Text(
                            text = "Localización: ${locker?.location},${locker?.city} ",
                            color = Color.Black,
                            modifier = Modifier.clickable {
                                val addressClick = "geo:0,0?q=${locker?.latitude},${locker?.longitude}(${locker?.location})".toUri()
                                val mapIntent = Intent(Intent.ACTION_VIEW, addressClick)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                startActivity(context, mapIntent, null)
                            }
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Precio: ${payment?.amount}€", color = Color.Black)
                        Spacer(modifier = Modifier.size(8.dp))
                        CountDown(rental?.endDate)
                        Spacer(modifier = Modifier.size(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    rental?.let {
                                        rentalViewModel.finalizeSpecificRental(it, false)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp)
                            ) {
                                Text(
                                    text = "Cancelar reserva",
                                    color = White,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }

                            Button(
                                onClick = {
                                    rental?.let {
                                        rentalViewModel.finalizeSpecificRental(it, true)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "Finalizar reserva",
                                    color = White,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun convertDateToString(endDate: Date?): String {
    val instant: Instant = endDate?.toInstant() ?: Date().toInstant()
    val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return localDateTime.format(formatter)
}

@Composable
fun CountDown(endDate: Date?) {
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
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardHistoricRents(
    historicRental: HistoricRental?,
    lockersViewModel: LockersViewModel,
    historicalRentalViewModel: HistoricalRentalViewModel
) {
    var isSelected by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

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
        Log.e("HistoricRental", historicRental.toString())

        Column(
            modifier = Modifier
                .background(BeigeClaro)
                .padding(12.dp)
        ) {

            if (historicRental?.status == true) {
                textStatus = "Pagado"
                colorStatus = Color.Green

            } else {
                textStatus = "Cancelado"
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
                    contentDescription = "flecha",
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
                    text = "Número de tarjeta: ${historicRental?.cardNumber.toString()}",
                    color = Color.Black
                )
                if (historicRental?.calificado != true && historicRental?.status == true) {

                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(8.dp)
                    ) {
                        Text("Valorar Locker", color = White)
                    }
                    RatingDialog(
                        showDialog = showDialog,
                        onDismiss = { showDialog = false },
                        onSubmit = { puntuacion ->
                            lockersViewModel.guardarPuntuacionEnFirestore(
                                lockerId = historicRental.lockerId,
                                nuevaPuntuacion = puntuacion
                            )
                            historicalRentalViewModel.setStatus(historicRental, true)
                        }
                    )

                }
            }
        }
    }

}

@Composable
fun RatingDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (Float) -> Unit
) {
    var rating by remember { mutableStateOf(0f) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            containerColor = BeigeClaro,
            title = {
                Text(
                    "¿Qué te pareció este locker?",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    color = Color.Black
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RatingBar(
                        value = rating,
                        onValueChange = { rating = it },
                        onRatingChanged = { rating = it },
                        config = RatingBarConfig()
                            .activeColor(Primary)
                            .inactiveColor(Secondary)
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            onSubmit(rating)
                            onDismiss()
                        },
                        modifier = Modifier
                            .padding(end = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Enviar puntuación", color = White)
                    }
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Cancelar", color = White)
                    }
                }

            }
        )
    }
}

@Composable
fun IconDesplegable(total: Int, payed: Int,canceled:Int) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Options",
            modifier = Modifier.clickable {
                expanded = true
            }
                .padding(8.dp)

        )
        DropdownMenu(
            expanded=expanded,
            onDismissRequest = {
                expanded=false
            },
            modifier = Modifier
                .background(BeigeClaro)
                .padding(8.dp)
        ) {
            Column (
                modifier = Modifier.fillMaxWidth()

            ){
                Text("Total = $total", color = Color.Black, fontWeight = FontWeight.Bold)
                Text("Pagados = $payed",color = Color.Black, fontWeight = FontWeight.Bold)
                Text("Cancelados = $canceled",color = Color.Black, fontWeight = FontWeight.Bold)

            }

        }
    }
}