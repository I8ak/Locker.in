package com.example.lockerin.presentation.ui.screens.locker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.CityDropdown
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddLockerScreen(
    userID: String,
    navController: NavHostController,
    userViewModel: UsersViewModel = koinViewModel(),
    lockerViewModel: LockersViewModel = koinViewModel(),
    authViewModel: AuthViewModel=koinViewModel()
) {
    userViewModel.getUserById(userID)
    val userId = authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()
    val user=userViewModel.getUserById(userId.toString())
    var lockerID by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(true) }
    var size by remember { mutableStateOf("") }
    var dimension by remember { mutableStateOf("") }
    var pricePerHour by remember { mutableDoubleStateOf(0.0) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    DrawerMenu(
        textoBar = "Añadir Locker",
        navController = navController,
        authViewModel = viewModel(),
        fullUser = userState,
        content = { paddingValues ->
            Scaffold(
                modifier = Modifier
                    .statusBarsPadding()
                .padding(paddingValues),
                snackbarHost = { SnackbarHost(snackbarHostState) },
                content = { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BeigeClaro)
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = lockerID,
                            onValueChange = { lockerID = it },
                            label = {
                                Text(
                                    text = "Locker ID",
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

                        CityDropdown(selectedCity = city, onCitySelected = { city = it })



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
                        Button(
                            onClick = {
                                val newLocker = Locker(
                                    lockerID.capitalize(),
                                    address.capitalize(),
                                    city.capitalize(),
                                    status,
                                    size.capitalize(),
                                    dimension,
                                    pricePerHour
                                )
                                lockerViewModel.addLocker(newLocker) {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("El ID del locker ya existe.")
                                    }
                                }

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
                            Text(text = "Añadir Locker")
                        }
                    }
                }
            )
        },
    )
}


