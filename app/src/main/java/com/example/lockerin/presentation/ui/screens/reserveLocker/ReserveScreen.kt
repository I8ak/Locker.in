package com.example.lockerin.presentation.ui.screens.reserveLocker

import android.app.TimePickerDialog
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lockerin.R
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.lockers.RentalViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.font.FontWeight
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Date
import java.time.Duration
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReserveScreen(
    userID: String,
    navController: NavHostController,
    city: String,
    lockersViewModel: LockersViewModel = koinViewModel(),
    userViewModel: UsersViewModel= koinViewModel()
) {
    val user = userViewModel.getUserById(userID)
    val lockers = lockersViewModel.lockers.collectAsState()
    // Estados
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var startTime by remember { mutableStateOf<LocalTime?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var endTime by remember { mutableStateOf<LocalTime?>(null) }

    // Errores
    var dateError by remember { mutableStateOf(false) }
    var timeError by remember { mutableStateOf(false) }
    var dateStartError by remember { mutableStateOf(false) }
    var timeStartError by remember { mutableStateOf(false) }

    // Validación
    fun validate() {
        dateError = endDate != null && startDate != null && endDate!!.isBefore(startDate!!)
        dateStartError = startDate?.isBefore(LocalDate.now()) == true
        timeStartError = startDate?.isEqual(LocalDate.now()) == true &&
                startTime?.isBefore(LocalTime.now()) == true
        timeError = if (endDate != null && startDate != null && endDate == startDate) {
            endTime != null && startTime != null && endTime!!.isBefore(startTime)
        } else {
            false
        }
    }


    // Efectos para validación
    LaunchedEffect(startDate, endDate, startTime, endTime) {
        validate()
    }

    DrawerMenu(
        textoBar = city,
        navController = navController,
        authViewModel = viewModel(),
        fullUser = user,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Grupo Inicio
                Text(
                    "Inicio",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Color.Black
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DateSelector(
                        selectedDate = startDate,
                        onDateSelected = { startDate = it },
                        modifier = Modifier.weight(0.6f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    TimeSelector(
                        selectedTime = startTime,
                        onTimeSelected = { startTime = it },
                        modifier = Modifier.weight(0.4f)
                    )
                }

                // Grupo Fin
                Text(
                    "Fin",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    color = Color.Black
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DateSelector(
                        selectedDate = endDate,
                        onDateSelected = { endDate = it },
                        isError = dateError,
                        modifier = Modifier.weight(0.6f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    TimeSelector(
                        selectedTime = endTime,
                        onTimeSelected = { endTime = it },
                        isError = timeError,
                        modifier = Modifier.weight(0.4f)
                    )
                }

                // Mensajes de error
                if (dateStartError) {
                    Text(
                        text = "La fecha de inicio no puede ser anterior a la fecha actual",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                if (timeStartError) {
                    Text(
                        text = "La hora de inicio no puede ser anterior a la hora actual",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                if (dateError) {
                    Text(
                        text = "La fecha de fin no puede ser anterior a la de inicio",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                if (timeError) {
                    Text(
                        text = "La hora de fin no puede ser anterior a la de inicio",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Log.i("Fecha","fecha incio $startDate")
                Log.i("Fecha","hora incio $startTime")
                Log.i("Fecha","fecha fin $endDate")
                Log.i("Fecha","hora fin $endTime")
                // Dentro de tu ReserveScreen, reemplaza la condición actual con esta versión mejorada:
                if (startDate != null && startTime != null && endDate != null && endTime != null &&
                    !dateStartError && !timeStartError && !dateError && !timeError
                ) {

                    val rentalViewModel: RentalViewModel = koinViewModel()

                    // Convertir LocalDate a Date para usar en isLockerAvailable
                    val startDateTime = startDate!!.atTime(startTime!!)
                    val startDateAsDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant())

                    val durationstartDateTime = startDate?.atTime(startTime ?: LocalTime.MIDNIGHT)
                    val durationendDateTime = endDate?.atTime(endTime ?: LocalTime.MIDNIGHT)
                    val duration = CalcultaionDuracion(durationstartDateTime!!, durationendDateTime!!)

                    Log.i("Duracion","duracion ${duration}")
                    LazyColumn(
                        modifier = Modifier
                            .padding(paddingValues)
                            .background(Color.Transparent)
                    ) {
                        items(lockers.value.filter { it.city.equals(city, ignoreCase = true) }) { locker ->
                            key(locker.lockerID) {
                                LockersCard(
                                    userID = userID,
                                    locker = locker,
                                    date = startDateAsDate,
                                    rentalViewModel = rentalViewModel,
                                    city = city,
                                    navController = navController,
                                    duration = duration,
                                    startDate = TrasformarFecha(durationstartDateTime),
                                    endDate = TrasformarFecha(durationendDateTime)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
@RequiresApi(Build.VERSION_CODES.O)
fun CalcultaionDuracion(startDate: LocalDateTime, endDate: LocalDateTime): Double {
    val duration = Duration.between(startDate, endDate)
    return duration.toMinutes().toDouble()/60
}

@RequiresApi(Build.VERSION_CODES.O)
fun TrasformarFecha(date: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    return date.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    isError: Boolean = false,  // Nuevo parámetro para errores
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "",
            onValueChange = {},
            label = { Text("Fecha", color = Color.Black) },
            readOnly = true,
            enabled = false,
            isError = isError,  // Activa el estado de error
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Fecha",
                    tint = Color.Black  // Icono siempre negro
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showPicker = true },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,  // Texto negro
                disabledBorderColor = if (isError) Color.Red else Color.Black,  // Borde rojo si hay error
                disabledLabelColor = Color.Black,  // Label negro
                disabledLeadingIconColor = Color.Black,  // Icono negro
                disabledSupportingTextColor = Color.Red  // Color para mensajes de error
            )
        )

        if (isError) {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

    if (showPicker) {
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        )
                    }
                    showPicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeSelector(
    selectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    OutlinedTextField(
        value = selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "",
        onValueChange = {},
        label = { Text("Hora", color = Color.Black) },
        readOnly = true,
        enabled = false,
        isError = isError,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Hora",
                tint = Color.Black
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable { showPicker = true },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledBorderColor = if (isError) Color.Red else Color.Black,
            disabledLabelColor = Color.Black,
            disabledLeadingIconColor = Color.Black,
            disabledSupportingTextColor = Color.Red
        )
    )

    if (showPicker) {
        val timePicker = remember {
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    onTimeSelected(LocalTime.of(hour, minute))
                    showPicker = false
                },
                selectedTime?.hour ?: 12,
                selectedTime?.minute ?: 0,
                true
            )
        }

        LaunchedEffect(showPicker) {
            if (showPicker) timePicker.show()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LockersCard(userID: String, locker: Locker, date: Date?, rentalViewModel: RentalViewModel,city: String,navController: NavHostController,duration: Double,startDate: String,endDate: String) {
    val imagen = when (locker.size) {
        "Small" -> R.drawable.personal_bag
        "Medium" -> R.drawable.luggage
        else -> R.drawable.trolley
    }
    val isAvailable = rentalViewModel.isLockerAvailable(locker.lockerID,date,city)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .clickable { if (isAvailable) navController.navigate(Screen.Details.createRoute(userID,locker.lockerID,startDate,endDate,(duration*locker.pricePerHour).toString())) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imagen),
                contentDescription = "size ${locker.size}",
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterVertically)
            )
            Column (Modifier.padding(8.dp).fillMaxHeight()) {

                Log.i("Fecha","isAvailable $isAvailable")
                Text(
                    text = if (isAvailable) "Disponible" else "No disponible",
                    color = if (isAvailable) Color.Green else Color.Red,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Dirección: ${locker.location}", color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Dimensiones: ${locker.dimension}", color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Precio por hora: ${locker.pricePerHour}", color = Color.Black)
            }
        }
    }
}


//@RequiresApi(Build.VERSION_CODES.O)
//@Preview
//@Composable
//fun ReserveScreenPreview() {
//    val navController = rememberNavController()
//    ReserveScreen(
//        navController = navController,
//        city = "Madrid"
//    )
//}

