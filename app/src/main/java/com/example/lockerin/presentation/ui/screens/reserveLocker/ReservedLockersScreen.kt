package com.example.lockerin.presentation.ui.screens.reserveLocker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lockerin.R
import com.example.lockerin.data.utils.NetworkUtils
import com.example.lockerin.domain.model.HistoricRental
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.domain.model.Payment
import com.example.lockerin.domain.model.Rental
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.components.LoadingScreen
import com.example.lockerin.presentation.ui.components.NoConexionDialog
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.ui.theme.Secondary
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.payment.PaymentViewModel
import com.example.lockerin.presentation.viewmodel.rentals.HistoricalRentalViewModel
import com.example.lockerin.presentation.viewmodel.rentals.RentalViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
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
    authViewModel: AuthViewModel = viewModel()
) {
    // Maneja el botón de retroceso para navegar a la pantalla de inicio.
    BackHandler {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
        }
    }

    // Recopila el estado del usuario actual.
    val userId = authViewModel.currentUserId
    val userState by usersViewModel.user.collectAsState()
    usersViewModel.getUserById(userId.toString())



    // Recopila los estados de los alquileres, casilleros, pagos e historial.
    val rentalState by rentalViewModel.rentals.collectAsState()
    val lockers by lockersViewModel.lockers.collectAsState()
    val payments by paymentViewModel.payments.collectAsState()
    val historicRentalState by historicalRentalViewModel.historicalRental.collectAsState()
    val payedCount by historicalRentalViewModel.payedCount.collectAsState()
    val canceledCount by historicalRentalViewModel.canceledCount.collectAsState()

    // Efecto para inicializar los datos del usuario.
    LaunchedEffect(userID) {
        rentalViewModel.setUserId(userID)
        paymentViewModel.setUserId(userID)
        historicalRentalViewModel.setUserId(userID)
        historicalRentalViewModel.countHistoricRentals(userID)
        usersViewModel.getUserById(userID)
    }

    // Estado para controlar la pantalla de carga.
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = false
    }

    // Muestra la pantalla de carga si isLoading es verdadero.
    if (isLoading) {
        LoadingScreen(isLoading = true)
    } else {
        DrawerMenu(
            textoBar = "Reservas",
            navController = navController,
            authViewModel = viewModel(),
            fullUser = userState,
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    // Título de la sección de reservas activas.
                    Text(
                        text = "Reservas activas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )

                    // Lista de reservas activas.
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(rentalState) { rental ->
                            val locker = lockers.find { it.lockerID == rental.lockerID }
                            val payment = payments.find { it.rentalID == rental.rentalID }

                            key(rental.lockerID) {
                                Spacer(modifier = Modifier.size(8.dp))
                                CardReserved(
                                    locker = locker,
                                    rental = rental,
                                    payment = payment
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.size(20.dp))

                    // Sección del historial de reservas.
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Historial de reservas",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        )
                        // Icono desplegable para mostrar estadísticas del historial.
                        IconDesplegable(
                            total = historicRentalState.size,
                            payed = payedCount,
                            canceled = canceledCount
                        )
                    }

                    // Lista del historial de reservas.
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(historicRentalState) { historicRental ->
                            key(historicRental.historicID) {
                                Spacer(modifier = Modifier.size(8.dp))
                                CardHistoricRents(
                                    historicRental = historicRental,
                                    lockersViewModel = lockersViewModel,
                                    historicalRentalViewModel = historicalRentalViewModel
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
    // Estado para controlar la expansión de la tarjeta.
    var isExpanded by remember { mutableStateOf(false) }

    // Determina la imagen del casillero según su tamaño.
    val imageResId = when (locker?.size) {
        "Small" -> R.drawable.personal_bag
        "Medium" -> R.drawable.luggage
        else -> R.drawable.trolley
    }

    val context = LocalContext.current
    var showDialogConection by remember { mutableStateOf(false) }
    if (showDialogConection){
        NoConexionDialog(
            onDismiss = { showDialogConection = false }
        )
    }

    // Efecto para obtener la información de la tarjeta de crédito.
    LaunchedEffect(payment?.cardID) {
        cardsViewModel.getCardById(payment?.cardID.toString())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .clickable { isExpanded = !isExpanded },
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
                    painter = painterResource(id = imageResId),
                    contentDescription = "imagen ${locker?.size}",
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = rental?.rentalID ?: "ID no disponible",
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Hasta ${convertDateToString(rental?.endDate)}",
                        modifier = Modifier.padding(top = 8.dp),
                        color = Color.Black
                    )
                    if (isExpanded) {
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Localización: ${locker?.location}, ${locker?.city}",
                            color = Color.Black,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    val addressUri =
                                        "geo:0,0?q=${locker?.latitude},${locker?.longitude}(${locker?.location})".toUri()
                                    val mapIntent = Intent(Intent.ACTION_VIEW, addressUri)
                                    mapIntent.setPackage("com.google.android.apps.maps")
                                    startActivity(context, mapIntent, null)
                                } else {
                                    showDialogConection = true
                                }
                            }
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(text = "Precio: ${payment?.amount ?: "No disponible"}€", color = Color.Black)
                        Spacer(modifier = Modifier.size(4.dp))
                        CountDown(endDate = rental?.endDate)
                        Spacer(modifier = Modifier.size(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    rental?.let {
                                        if (NetworkUtils.isInternetAvailable(context)) {
                                            rentalViewModel.finalizeSpecificRental(it, false)
                                        } else {
                                            showDialogConection = true
                                        }
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
                                        if (NetworkUtils.isInternetAvailable(context)) {
                                            rentalViewModel.finalizeSpecificRental(it, true)
                                        } else {
                                            showDialogConection = true
                                        }

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
    var timeLeftString by remember { mutableStateOf("Calculando...") }

    LaunchedEffect(endDate) {
        if (endDate == null) {
            timeLeftString = "No hay fecha"
            return@LaunchedEffect
        }

        val endTime = endDate.time
        while (true) {
            val timeLeft = endTime - System.currentTimeMillis()
            if (timeLeft <= 0) {
                timeLeftString = "Finalizado"
                break
            } else {
                val totalSeconds = timeLeft / 1000
                val seconds = totalSeconds % 60
                val totalMinutes = totalSeconds / 60
                val minutes = totalMinutes % 60
                val totalHours = totalMinutes / 60
                val hours = totalHours % 24
                val days = totalHours / 24

                timeLeftString = buildString {
                    if (days > 0) append("${days}d ")
                    if (hours > 0 || days > 0) append("${hours}h ")
                    if (minutes > 0 || hours > 0 || days > 0) append("${minutes}m ")
                    append("${seconds}s")
                }.trim()

                delay(1000)
            }
        }
    }
    Text(text = "Finaliza en $timeLeftString", color = Color.Black, fontSize = 20.sp)
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardHistoricRents(
    historicRental: HistoricRental?,
    lockersViewModel: LockersViewModel,
    historicalRentalViewModel: HistoricalRentalViewModel
) {
    // Estado para controlar la expansión de la tarjeta.
    var isExpanded by remember { mutableStateOf(false) }
    // Estado para controlar la visualización del diálogo de valoración.
    var showDialog by remember { mutableStateOf(false) }

    // Determina el texto y color del estado del alquiler.
    val (statusText, statusColor) = if (historicRental?.status == true) {
        "Pagado" to Color.Green
    } else {
        "Cancelado" to Color.Red
    }

    // Determina el icono de la flecha según el estado de expansión.
    val arrowIcon = if (isExpanded) {
        Icons.Default.KeyboardDoubleArrowUp
    } else {
        Icons.Default.KeyboardDoubleArrowDown
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .clickable { isExpanded = !isExpanded },
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = statusText, color = statusColor, fontSize = 20.sp)
                Icon(
                    imageVector = arrowIcon,
                    contentDescription = "flecha",
                    tint = Color.Black,
                    modifier = Modifier.clickable { isExpanded = !isExpanded }
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = "Precio total: ${historicRental?.amount ?: "No disponible"}€", color = Color.Black)
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Fecha de reserva: ${convertDateToString(historicRental?.createdAt)}",
                color = Color.Black
            )
            if (isExpanded) {
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
                    text = "Número de tarjeta: ${historicRental?.cardNumber ?: "No disponible"}",
                    color = Color.Black
                )
                // Botón para valorar el casillero si no ha sido calificado y el estado es pagado.
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
                    // Diálogo para la valoración del casillero.
                    RatingDialog(
                        showDialog = showDialog,
                        onDismiss = { showDialog = false },
                        onSubmit = { rating ->
                            lockersViewModel.guardarPuntuacionEnFirestore(
                                lockerId = historicRental.lockerId,
                                nuevaPuntuacion = rating
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
    // Estado para almacenar la valoración.
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
fun IconDesplegable(total: Int, payed: Int, canceled: Int) {
    var isExpanded by remember { mutableStateOf(false) }

    Box {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Options",
            modifier = Modifier
                .clickable { isExpanded = true }
                .padding(8.dp)
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .background(BeigeClaro)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Total = $total", color = Color.Black, fontWeight = FontWeight.Bold)
                Text("Pagados = $payed", color = Color.Black, fontWeight = FontWeight.Bold)
                Text("Cancelados = $canceled", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}
