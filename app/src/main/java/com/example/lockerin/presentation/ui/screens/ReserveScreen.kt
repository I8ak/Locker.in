package com.example.lockerin.presentation.ui.screens

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.presentation.ui.components.DrawerMenu
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReserveScreen(
    navController: NavHostController,
    city: String
) {
    // Estados
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var startTime by remember { mutableStateOf<LocalTime?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var endTime by remember { mutableStateOf<LocalTime?>(null) }

    // Errores
    var dateError by remember { mutableStateOf(false) }
    var timeError by remember { mutableStateOf(false) }

    // Validación
    fun validate() {
        dateError = endDate != null && startDate != null && endDate!!.isBefore(startDate!!)
        timeError = if (endDate != null && startDate != null && endDate!!.isEqual(startDate!!)) {
            endTime != null && startTime != null && endTime!!.isBefore(startTime!!)
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
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Grupo Inicio
                Text("Inicio",
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
                Text("Fin",
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
            }
        }
    )
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

        // Espacio reservado para mensaje de error
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
                    Text("OK", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text("Cancelar", color = Color.Black)
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
@Preview
@Composable
fun ReserveScreenPreview() {
    val navController = rememberNavController()
    ReserveScreen(
        navController = navController,
        city = "Lima"
    )
}

