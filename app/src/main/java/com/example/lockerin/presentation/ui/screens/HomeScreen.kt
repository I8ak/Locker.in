package com.example.lockerin.presentation.ui.screens

import android.os.Build
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
import com.example.lockerin.presentation.viewmodel.lockers.RentalViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import androidx.compose.runtime.getValue
import com.example.lockerin.presentation.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    appViewModel: AppViewModel,
    authViewModel: AuthViewModel = viewModel(),
    userViewModel: UsersViewModel= koinViewModel(),
    lockersViewModel: LockersViewModel = koinViewModel(),
    rentalViewModel: RentalViewModel=koinViewModel(),
) {

    val userId = authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()
    val rentalCount by rentalViewModel.rentalCount.collectAsState()
    val counts by lockersViewModel.availableCounts.collectAsState()
    LaunchedEffect(Unit) {
        lockersViewModel.countAvailableLockersByCity("Madrid")
        lockersViewModel.countAvailableLockersByCity("Barcelona")
        userId?.let {
            rentalViewModel.checkAndMoveExpiredRentals(it)
        }
        userViewModel.getUserById(userId.toString())
    }

    LaunchedEffect(userState, counts, rentalCount) { // Observa los estados de los datos
        // Una lógica más robusta para determinar si la pantalla está lista:
        val areCountsLoaded = counts.isNotEmpty() // O verifica si contiene ambas ciudades si siempre deben estar
        val isUserLoaded = userState != null // O verifica un estado de carga en UserViewModel
        val isRentalCountLoaded = rentalCount != -1 // Asumiendo que -1 es un valor inicial o no válido

        // Considera que la pantalla está lista cuando los datos clave se han cargado
        if (isUserLoaded && areCountsLoaded && isRentalCountLoaded) {
            appViewModel.setLoading(false) // Desactivar el loading global
        }
    }

    val cantidadMadrid = counts["Madrid"] ?: 0
    val cantidadBarcelona = counts["Barcelona"] ?: 0

    DrawerMenu(
        textoBar = "Ciudades",
        navController = navController,
        authViewModel = authViewModel,
        fullUser = userState,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
            ) {
                userId?.let { nonNullUserId ->
                    Reservas(userID = nonNullUserId, navController = navController)
                }
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
    )

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Reservas(
    userID: String,
    navController: NavController) {


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
        // Icono de locker
        Image(
            painter = painterResource(id = R.drawable.locker),
            contentDescription = "Icono de reservas",
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Columna con textos
        Column {
            Text(
                text = "Reservas",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.Black
            )

            Text(
                text = "${rentalCount} ${if (rentalCount == 1) "Reserva disponible" else "Reservas disponibles"}",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Black
            )


        }
    }
}


@Composable
fun CiudadCard(userId: String,nombre: String, cantidad: Int, imagen: Int, navController: NavHostController) {
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

