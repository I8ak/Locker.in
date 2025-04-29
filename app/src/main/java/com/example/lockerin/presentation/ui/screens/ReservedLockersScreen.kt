package com.example.lockerin.presentation.ui.screens

import android.os.Build
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.R
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.domain.model.Payment
import com.example.lockerin.domain.model.Rental
import com.example.lockerin.domain.model.User
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.lockers.RentalViewModel
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.payment.PaymentViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservedLockersScreen(
    userID: String,
    rentalID: String,
    lockerID: String,
    navController: NavHostController
) {
    val lockersViewModel: LockersViewModel = viewModel()
    val rentalViewModel: RentalViewModel = viewModel()
    val paymentViewModel: PaymentViewModel = viewModel()
    val usersViewModel:UsersViewModel = viewModel()

    val user = usersViewModel.getUserById(userID)
    val locker = lockersViewModel.getLockerById(lockerID)
    val rental = rentalViewModel.getRentalById(rentalID)
    val payment = paymentViewModel.getPaymentByUserId(userID)

    DrawerMenu(
        textoBar = "Reservas",
        navController = navController,
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
                Spacer(modifier = Modifier.size(8.dp))
                items { rental?.rentalID->
                    key(rentalId) {
                        CardReserved(
                            locker,
                            rental,
                            payment
                        )
                    }

                }




                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    text = "Historial de reservas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                CardHistoricRents(
                    user
                )

            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardReserved(
    locker: Locker?,
    rental: Rental?,
    payment: Payment?
) {

    var isSelected by remember { mutableStateOf(false) }
    val imagen = when (locker?.size) {
        "Small" -> R.drawable.personal_bag
        "Medium" -> R.drawable.luggage
        else -> R.drawable.trolley
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
                        text = locker?.city ?: "",
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
                        Text(text = "Localización: ${locker?.location}", color = Color.Black)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Precio: ${payment?.amount}€", color = Color.Black)
                        Spacer(modifier = Modifier.size(8.dp))
                        CountDown(rental?.endDate)
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
    user: User?
){
    var isSelected by remember { mutableStateOf(false) }
    val lockersViewModel: LockersViewModel = viewModel()
    val rentalViewModel: RentalViewModel = viewModel()
    val paymentViewModel: PaymentViewModel = viewModel()
    val cardsViewModel: CardsViewModel = viewModel()
    val userID = user?.userID ?: ""
    val lockerID= rentalViewModel.getLockerByUserId(userID)
    val locker = lockersViewModel.getLockerById(lockerID)
    val rental = rentalViewModel.getRentalByUserId(userID)
    val payment = paymentViewModel.getPaymentByUserId(userID)
    val card = cardsViewModel.getCardByUserId(userID)
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

                if (payment?.status ==true){
                    textStatus= "Pagado"
                    colorStatus= Color.Green

                }else{
                    textStatus= "Pendiente"
                    colorStatus= Color.Red
                }
                Row (
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
                Text(text = "Precio total: ${payment?.amount}€", color = Color.Black)
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = "Fecha de reserva: ${convertDateToString(payment?.date)}", color = Color.Black)
                if (isSelected) {
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = "Localización: ${locker?.location} ${locker?.city}", color = Color.Black)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = "Fecha de inicio: ${convertDateToString(rental?.startDate)}", color = Color.Black)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = "Fecha de fin: ${convertDateToString(rental?.endDate)}", color = Color.Black)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = "Tipo de casillero: ${locker?.size} ${locker?.dimension}", color = Color.Black)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = "Número de tarjeta: ${cardsViewModel.hasNumberCard(card?.cardNumber.toString())}", color = Color.Black)

                }
            }


    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ReserverScreenPreview() {
    ReservedLockersScreen("1", "1", "locker1", rememberNavController())
}