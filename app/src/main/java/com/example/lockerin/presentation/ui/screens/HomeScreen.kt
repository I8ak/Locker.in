package com.example.lockerin.presentation.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.R
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.rentals.RentalViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.lockerin.presentation.ui.components.LoadingScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.system.exitProcess

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    userViewModel: UsersViewModel = koinViewModel(),
    lockersViewModel: LockersViewModel = koinViewModel(),
    rentalViewModel: RentalViewModel = koinViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var backPressedOnce by remember { mutableStateOf(false) }

    BackHandler {
        if (backPressedOnce) {
            exitProcess(0)
        } else {
            backPressedOnce = true
            scope.launch {
                snackbarHostState.showSnackbar("Presiona atrás otra vez para salir")
            }
        }
    }
    LaunchedEffect(Unit) {
        delay(2000)
        backPressedOnce = false
    }

    val userId = authViewModel.currentUserId
    if (userId == null) {
        navController.navigate(Screen.Login.route)
        return
    }
    val userState by userViewModel.user.collectAsState()
    val counts by lockersViewModel.availableCounts.collectAsState()
    LaunchedEffect(userId) {
        lockersViewModel.countAvailableLockersByCity("Madrid")
        lockersViewModel.countAvailableLockersByCity("Barcelona")
        userId.let {
            rentalViewModel.checkAndMoveExpiredRentals(it)
        }
        userViewModel.getUserById(userId.toString())
    }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = false
    }


    val cantidadMadrid = counts["Madrid"] ?: 0
    val cantidadBarcelona = counts["Barcelona"] ?: 0

    if (isLoading) {
        LoadingScreen(isLoading = isLoading)
    } else {
        DrawerMenu(
            textoBar = "Ciudades",
            navController = navController,
            authViewModel = authViewModel,
            fullUser = userState,
            content = { padding->
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    containerColor = BeigeClaro
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(padding)
                    ) {
                        Reservas(userID = userId, navController = navController)
                        Spacer(modifier = Modifier.weight(1f))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            CiudadCard(
                                userId = userId.toString(),
                                nombre = "Madrid",
                                cantidad = cantidadMadrid,
                                imagen = R.drawable.madrid,
                                navController = navController
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            CiudadCard(
                                userId = userId.toString(),
                                nombre = "Barcelona",
                                cantidad = cantidadBarcelona,
                                imagen = R.drawable.barcelona,
                                navController = navController
                            )
                        }
                        Spacer(modifier = Modifier.weight(2f))
                    }
                }
            }
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Reservas(
    userID: String,
    navController: NavController
) {

    val rentalViewModel: RentalViewModel = koinViewModel()
    val rentalCount by rentalViewModel.rentalCount.collectAsState()

    LaunchedEffect(userID) {
        rentalViewModel.countRentals(userID)
    }

    Row(
        modifier = Modifier
            .background(BeigeClaro)
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(6.dp)
            .clickable {
                navController.navigate(Screen.ResrvedLockers.createRoute(userID))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.locker),
            contentDescription = "Icono de reservas",
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "Reservas",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.Black
            )

            Text(
                text = "$rentalCount ${if (rentalCount == 1) "Reserva disponible" else "Reservas disponibles"}",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Black
            )


        }
    }
}


@Composable
fun CiudadCard(
    userId: String,
    nombre: String,
    cantidad: Int,
    imagen: Int,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .background(BeigeClaro)
            .fillMaxWidth()
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
            .clickable {
                navController.navigate(Screen.Reserve.createRoute(userID = userId, city = nombre))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imagen),
            contentDescription = "imagen $nombre",
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.Black
            )
            Text(
                text = "$cantidad ${if (cantidad == 1) "Locker disponible" else "Lockers disponibles"}",
                modifier = Modifier.padding(top = 8.dp), color = Color.Black
            )
        }
    }
}


