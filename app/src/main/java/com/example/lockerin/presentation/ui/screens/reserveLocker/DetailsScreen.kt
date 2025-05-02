package com.example.lockerin.presentation.ui.screens.reserveLocker

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    userId: String,
    lockerID: String,
    startDate: String,
    endDate: String,
    totalPrice: String,
    navController: NavHostController= rememberNavController(),
    userViewModel: UsersViewModel= koinViewModel()
){
    val user= userViewModel.getUserById(userId)
    Log.d("DetailsScreen", totalPrice.toString())
    val lockersViewModel: LockersViewModel= koinViewModel()
    var locker by remember { mutableStateOf<Locker?>(null) }
    LaunchedEffect(lockerID) {
        locker = lockersViewModel.getLockerById(lockerID)
    }
    DrawerMenu(
        textoBar = "Datos de la Reserva",
        navController = navController,
        authViewModel = viewModel(),
        fullUser = user,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {




                locker?.let {
                    // Fecha de inicio
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Fecha de inicio",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color.LightGray)
                                .padding(8.dp)
                                .height(50.dp)
                                .weight(1f),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = startDate,
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .padding(8.dp)
                                .height(50.dp)
                                .weight(1f),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

                    // Fecha de fin
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Fecha de fin",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color.LightGray)
                                .padding(8.dp)
                                .height(50.dp) // Aumenta la altura del Text
                                .weight(1f), // Centrado vertical
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = endDate,
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .padding(8.dp)
                                .weight(1f)
                                .height(50.dp) ,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

// Ubicación
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Ubicación",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color.LightGray)
                                .padding(8.dp)
                                .height(50.dp) // Aumenta la altura del Text
                                .weight(1f),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = it.location,
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .padding(8.dp)
                                .weight(1f)
                                .height(50.dp), // Aumenta la altura del Text
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

// Dimensiones
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .heightIn(min = 50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Dimensiones",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color.LightGray)
                                .padding(8.dp)
                                .height(50.dp) // Aumenta la altura del Text
                                .weight(1f),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = it.dimension,
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .padding(8.dp)
                                .weight(1f)
                                .height(50.dp),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

                    // Precio total
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Precio total",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color.LightGray)
                                .padding(8.dp)
                                .height(50.dp) // Aumenta la altura del Text
                                .weight(1f),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$totalPrice €",
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .padding(8.dp)
                                .weight(1f)
                                .height(50.dp), // Aumenta la altura del Text
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            navController.navigate(Screen.Payment.createRoute("1", lockerID, startDate, endDate, totalPrice))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text(text = "Reservar", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                } ?: run {
                    Text(text = "No se encontró el locker con ID: $lockerID")
                }
            }
        }
    )
}

//@Composable
//@Preview
//fun DetailsScreenPreview(){
//    DetailsScreen(
//        lockerID = "locker1",
//        startDate = "2023-10-01",
//        endDate = "2023-10-02",
//        totalPrice = "10.0"
//    )
//}