package com.example.lockerin.presentation.ui.screens.locker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditLockerScreen(
    userID: String,
    lockerID: String,
    navController: NavHostController,
    userViewModel: UsersViewModel = koinViewModel(),
    lockerViewModel: LockersViewModel = koinViewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    userViewModel.getUserById(userID)

    val userId = authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()
    val user = userViewModel.getUserById(userId.toString())
    val locker by lockerViewModel.selectedLocker.collectAsState()

    LaunchedEffect(lockerID) {
        lockerViewModel.getLockerById(lockerID)
    }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(true) }
    var size by remember { mutableStateOf("") }
    var dimension by remember { mutableStateOf("") }
    var pricePerHour by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }
    var latitude by remember { mutableStateOf(0.0) }

    val focusManager = LocalFocusManager.current
    val addressFocusRequest= remember { FocusRequester() }
    val cityFocusRequest= remember { FocusRequester() }
    val sizeFocusRequest= remember { FocusRequester() }
    val dimensionFocusRequest= remember { FocusRequester() }
    val pricePerHourFocusRequest= remember { FocusRequester() }
    val longitudeFocusRequest= remember { FocusRequester() }
    val latitudeFocusRequest= remember { FocusRequester() }

    LaunchedEffect(locker) {
        if (locker != null) {
            address = locker!!.location
            city = locker!!.city
            status = locker!!.status
            size = locker!!.size
            dimension = locker!!.dimension
            pricePerHour = locker!!.pricePerHour
            longitude = locker!!.longitude
            latitude = locker!!.latitude
        }
    }

    DrawerMenu(
        textoBar = "Añadir Locker",
        navController = navController,
        authViewModel = viewModel(),
        fullUser = userState,
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
                        ).focusRequester(addressFocusRequest),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            latitudeFocusRequest.requestFocus()
                        }
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
                    value = latitude.toString(),
                    onValueChange = { latitude = it.toDouble() },
                    label = {
                        Text(
                            text = "Latitud",
                            color = Color.Black
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(12.dp)
                        ).focusRequester(latitudeFocusRequest),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            longitudeFocusRequest.requestFocus()
                        }
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
                    value = longitude.toString(),
                    onValueChange = { longitude = it.toDouble() },
                    label = {
                        Text(
                            text = "Longitud",
                            color = Color.Black
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(12.dp)
                        ).focusRequester(longitudeFocusRequest),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            cityFocusRequest.requestFocus()
                        }
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
                CityDropdown(selectedCity = city,cityFocusRequester = cityFocusRequest,nextFocusRequester = sizeFocusRequest, onCitySelected = { city = it })
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
                        ).focusRequester(sizeFocusRequest),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            dimensionFocusRequest.requestFocus()
                        }
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
                        ).focusRequester(dimensionFocusRequest),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            pricePerHourFocusRequest.requestFocus()
                        }
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
                        ).focusRequester(pricePerHourFocusRequest),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.clearFocus()
                        }
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
                            status = status,
                            latitude = latitude,
                            longitude = longitude
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
