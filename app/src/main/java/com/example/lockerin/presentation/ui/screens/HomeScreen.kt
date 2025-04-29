package com.example.lockerin.presentation.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.lockers.RentalViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController()
) {

    DrawerMenu(
        textoBar = "Ciudades",
        navController = navController,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
            ) {

                Reservas()
                Spacer(modifier = Modifier.weight(1f))
                val listaCiudades: LockersViewModel = viewModel()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CiudadCard(
                        nombre = "Madrid",
                        cantidad = listaCiudades.countAvailableLockersByCity("Madrid"),
                        imagen = R.drawable.madrid,
                        navController = navController
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CiudadCard(
                        nombre = "Barcelona",
                        cantidad = listaCiudades.countAvailableLockersByCity("Barcelona"),
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
fun Reservas() {
    val listReservas: RentalViewModel = viewModel()

    Row(
        modifier = Modifier
            .background(BeigeClaro)
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(6.dp)
            .clickable {
//                navController.navigate(Screen.ResrvedLockers.route)
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
                text = "${listReservas.countLockers("1")} ${if (listReservas.countLockers("1") == 1) "Reserva disponible" else "Reservas disponibles"}",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Black
            )


        }
    }
}


@Composable
fun CiudadCard(nombre: String, cantidad: Int, imagen: Int, navController: NavHostController) {
    Row(
        modifier = Modifier
            .background(BeigeClaro)
            .fillMaxWidth()
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
            .clickable {
                navController.navigate(Screen.Reserve.createRoute(city = nombre))
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}