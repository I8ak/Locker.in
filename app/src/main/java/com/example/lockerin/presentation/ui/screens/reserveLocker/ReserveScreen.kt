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
import com.example.lockerin.presentation.viewmodel.rentals.RentalViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.text.font.FontWeight
import com.example.lockerin.data.utils.NetworkUtils
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.NoConexionDialog
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
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
    userViewModel: UsersViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    // Obtiene el ID del usuario actual y el estado del usuario.
    val userId = authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()
    userViewModel.getUserById(userId.toString())
    val lockers = lockersViewModel.lockers.collectAsState()

    // Estados para las fechas y horas seleccionadas.
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var startTime by remember { mutableStateOf<LocalTime?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var endTime by remember { mutableStateOf<LocalTime?>(null) }

    // Estados para los errores de validación.
    var dateError by remember { mutableStateOf(false) }
    var timeError by remember { mutableStateOf(false) }
    var dateStartError by remember { mutableStateOf(false) }
    var timeStartError by remember { mutableStateOf(false) }

    // Función de validación de fechas y horas.
    fun validate() {
        // Valida que la fecha de inicio no sea anterior a la fecha actual.
        dateStartError = startDate?.isBefore(LocalDate.now()) == true

        // Valida que la hora de inicio no sea anterior a la hora actual si la fecha de inicio es hoy.
        timeStartError = startDate?.isEqual(LocalDate.now()) == true &&
                startTime?.isBefore(LocalTime.now()) == true

        // Valida que la fecha de fin no sea anterior a la fecha de inicio.
        dateError = endDate != null && startDate != null && endDate!!.isBefore(startDate!!)

        // Valida que la hora de fin no sea anterior a la hora de inicio si las fechas son iguales.
        timeError = if (endDate != null && startDate != null && endDate == startDate) {
            endTime != null && startTime != null && endTime!!.isBefore(startTime)
        } else {
            false
        }
    }

    // Estado para la pantalla de carga.
    var isLoading by remember { mutableStateOf(false) }

    // Efecto que se ejecuta cuando cambian las fechas u horas para revalidar y actualizar el estado de carga.
    LaunchedEffect(startDate, startTime, endDate, endTime) {
        validate()

        // Solo se carga si todas las fechas y horas están seleccionadas y no hay errores de validación.
        if (startDate != null && startTime != null &&
            endDate != null && endTime != null &&
            !dateStartError && !timeStartError && !dateError && !timeError
        ) {
            isLoading = true
            kotlinx.coroutines.delay(500)
            isLoading = false
        }
    }

    DrawerMenu(
        textoBar = city,
        navController = navController,
        authViewModel = authViewModel,
        fullUser = userState,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Selector de Fecha de Inicio
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
                        isError = dateStartError,
                        modifier = Modifier.weight(0.6f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    TimeSelector(
                        selectedTime = startTime,
                        onTimeSelected = { startTime = it },
                        isError = timeStartError,
                        modifier = Modifier.weight(0.4f)
                    )
                }

                // Selector de Fecha de Fin
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

                // Mensajes de error para el usuario
                if (dateStartError) {
                    Text(
                        text = "La fecha de inicio no puede ser anterior a la fecha actual.",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                if (timeStartError) {
                    Text(
                        text = "La hora de inicio no puede ser anterior a la hora actual.",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                if (dateError) {
                    Text(
                        text = "La fecha de fin no puede ser anterior a la de inicio.",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                if (timeError) {
                    Text(
                        text = "La hora de fin no puede ser anterior a la de inicio (si es el mismo día).",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Logs para depuración
                Log.i("Fecha", "fecha inicio: $startDate")
                Log.i("Fecha", "hora inicio: $startTime")
                Log.i("Fecha", "fecha fin: $endDate")
                Log.i("Fecha", "hora fin: $endTime")

                // Muestra la lista de lockers solo si todas las selecciones son válidas y no hay errores
                if (startDate != null && startTime != null && endDate != null && endTime != null &&
                    !dateStartError && !timeStartError && !dateError && !timeError
                ) {
                    val rentalViewModel: RentalViewModel = koinViewModel()

                    // Convierte LocalDate y LocalTime a LocalDateTime y luego a Date para la lógica de disponibilidad.
                    val startDateTime = startDate!!.atTime(startTime!!)
                    val startDateAsDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant())
                    val endDateTime = endDate!!.atTime(endTime!!)
                    val endDateAsDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant())

                    val duration = calculateDuration(startDateTime, endDateTime)

                    Log.i("Duracion", "duracion: $duration horas")

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 32.dp),
                            color = Primary
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .background(Color.Transparent)
                        ) {
                            items(lockers.value.filter {
                                it.city.equals(city, ignoreCase = true)
                            }) { locker ->
                                key(locker.lockerID) {
                                    LockersCard(
                                        userID = userId.toString(),
                                        locker = locker,
                                        startDate = startDateAsDate,
                                        endDate = endDateAsDate,
                                        rentalViewModel = rentalViewModel,
                                        navController = navController,
                                        duration = duration,
                                        startDateString = formatDateTime(startDateTime),
                                        endDateString = formatDateTime(endDateTime),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateDuration(startDate: LocalDateTime, endDate: LocalDateTime): Double {
    val duration = Duration.between(startDate, endDate)
    return duration.toMinutes().toDouble() / 60
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(date: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    return date.format(formatter)
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    isError: Boolean = false,
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
            isError = isError,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Fecha",
                    tint = Color.Black
                )
            },
            modifier = Modifier
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
                selectedTime?.hour ?: LocalTime.now().hour,
                selectedTime?.minute ?: LocalTime.now().minute,
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
fun LockersCard(
    userID: String,
    locker: Locker,
    startDate: Date,
    endDate: Date,
    rentalViewModel: RentalViewModel,
    navController: NavHostController,
    duration: Double,
    startDateString: String,
    endDateString: String
) {
    // Determina la imagen del casillero según su tamaño.
    val imageResId = when (locker.size) {
        "Small" -> R.drawable.personal_bag
        "Medium" -> R.drawable.luggage
        else -> R.drawable.trolley
    }

    val context= LocalContext.current
    var showDialogConection by remember { mutableStateOf(false) }
    if (showDialogConection){
        NoConexionDialog(
            onDismiss = { showDialogConection = false }
        )
    }


    // Recopila el estado de disponibilidad del casillero del ViewModel.
    val lockerAvailabilityMap by rentalViewModel.lockerAvailability.collectAsState()
    val isAvailable = lockerAvailabilityMap[locker.lockerID] ?: false

    // Lanza el efecto para verificar la disponibilidad del casillero cuando cambian el ID del casillero o las fechas.
    LaunchedEffect(locker.lockerID, startDate, endDate) {
        Log.d("LockersCard", "Verificando disponibilidad para el casillero: ${locker.lockerID}")
        rentalViewModel.isLockerAvailable(locker.lockerID, startDate, endDate)
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .clickable {
                // Navega a la pantalla de detalles solo si el casillero está disponible.
                if (NetworkUtils.isInternetAvailable(context)) {
                    if (isAvailable) {
                        navController.navigate(
                            Screen.Details.createRoute(
                                userID,
                                locker.lockerID,
                                startDateString,
                                endDateString,
                                (duration * locker.pricePerHour).toString()
                            )
                        )
                    }
                } else {
                    showDialogConection = true
                }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Tamaño del casillero: ${locker.size}",
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterVertically)
            )
            Column(
                Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = if (isAvailable) "Disponible" else "No disponible",
                        color = if (isAvailable) Color.Green else Color.Red,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${locker.puntuacion}",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Puntuación",
                        tint = Primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Dirección: ${locker.location}", color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Dimensiones: ${locker.dimension}", color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Precio por hora: ${locker.pricePerHour} €", color = Color.Black)
            }
        }
    }
}