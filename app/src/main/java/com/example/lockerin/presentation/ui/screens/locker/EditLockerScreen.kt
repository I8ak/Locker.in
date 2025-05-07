package com.example.lockerin.presentation.ui.screens.locker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditLockerScreen(
    userID: String,
    lockerID: String,
    navController: NavHostController,
    userViewModel: UsersViewModel = koinViewModel(),
    lockerViewModel: LockersViewModel = koinViewModel()
) {
    userViewModel.getUserById(userID)
    var locker by remember { mutableStateOf<Locker?>(null) }

    LaunchedEffect(lockerID) {
        lockerViewModel.getLockerById(lockerID)
    }
    val user by userViewModel.user.collectAsState()
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(true) }
    var size by remember { mutableStateOf("") }
    var dimension by remember { mutableStateOf("") }
    var pricePerHour by remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(locker) {
        if (locker != null) {
            address = locker!!.location
            city = locker!!.city
            status = locker!!.status
            size = locker!!.size
            dimension = locker!!.dimension
            pricePerHour = locker!!.pricePerHour
        }
    }

    DrawerMenu(
        textoBar = "Añadir Locker",
        navController = navController,
        authViewModel = viewModel(),
        fullUser = user,
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BeigeClaro)
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = {
                        Text(
                            text = "Dirección",
                            color = Color.Black
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = {
                        Text(
                            text = "Ciudad",
                            color = Color.Black
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )


                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = size,
                    onValueChange = { size = it },
                    label = {
                        Text(
                            text = "Tamaño",
                            color = Color.Black
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = dimension,
                    onValueChange = { dimension = it },
                    label = {
                        Text(
                            text = "Dimensiones",
                            color = Color.Black
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = pricePerHour.toString(),
                    onValueChange = { pricePerHour = it.toDouble() },
                    label = {
                        Text(
                            text = "Precio por hora",
                            color = Color.Black
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Estado del Locker",
                    color = Color.Black
                )
                Switch(
                    checked = status,
                    onCheckedChange = { status = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Primary,
                        uncheckedThumbColor = Color.Gray,
                        checkedTrackColor = Primary.copy(alpha = 0.3f),
                        uncheckedTrackColor = Color.LightGray
                    ),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Spacer(modifier = Modifier.padding(5.dp))

                Button(
                    onClick = {
                        val editLocker = Locker(
                            lockerID = lockerID,
                            location = address,
                            city = city,
                            size = size,
                            dimension = dimension,
                            pricePerHour = pricePerHour,
                            status = status
                        )
                        lockerViewModel.editLocker(editLocker)
                        navController.navigate(Screen.ListLockers.createRoute(userID))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        disabledContentColor = Color.White.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(text = "Editar Locker")
                }
            }
        }
    )
}
