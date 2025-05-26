package com.example.lockerin.presentation.ui.screens.locker

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lockerin.R
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.domain.model.User
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.components.LoadingScreen
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListLockersScreen(
    userID: String,
    navController: NavHostController,
    userViewModel: UsersViewModel = koinViewModel(),
    lockerViewModel: LockersViewModel = koinViewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val userId = authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()
    val user = userViewModel.getUserById(userId.toString())
    userViewModel.getUserById(userID)

    // Recopilamos la lista de lockers disponibles
    val lockers by lockerViewModel.lockers.collectAsState()

    // Log para depuración
    Log.d("Locker en Screen", "Mapped Locker: $lockers")

    // Componente de menú lateral
    DrawerMenu(
        textoBar = "Lockers",
        navController = navController,
        authViewModel = viewModel(),
        fullUser = userState,
        content = { paddingValues ->
            Scaffold(
                content = { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(BeigeClaro)
                    ) {
                        // Lista desplazable de casilleros
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentPadding = paddingValues
                        ) {
                            items(lockers) { locker ->
                                key(locker.lockerID) {
                                    LockersCard(
                                        user = user,
                                        locker = locker,
                                        navController = navController,
                                        lockerViewModel = lockerViewModel
                                    )
                                }
                            }
                        }
                    }
                },
                floatingActionButton = {
                    // Botón flotante para añadir un nuevo casillero
                    Button(
                        onClick = {
                            navController.navigate(Screen.AddLocker.createRoute(userID))
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            disabledContentColor = Color.White.copy(alpha = 0.7f)
                        )
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "AddProduct"
                        )
                    }
                }
            )
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LockersCard(
    user: User?,
    locker: Locker?,
    navController: NavHostController,
    lockerViewModel: LockersViewModel
) {
    // Si el casillero o el usuario son nulos, no se renderiza la tarjeta
    if (locker == null || user == null) {
        Log.d("Locker en Card", "Mapped Locker: $locker y user: $user")
        return
    }
    // Log para depuración
    Log.d("Locker en Card", "Mapped Locker: $locker y user: $user")

    // Determina la imagen del casillero según su tamaño
    val imagen = when (locker.size) {
        "Small" -> R.drawable.personal_bag
        "Medium" -> R.drawable.luggage
        else -> R.drawable.trolley
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                // Imagen del tipo de casillero
                Image(
                    painter = painterResource(id = imagen),
                    contentDescription = "size ${locker.size}",
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Top)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    // Información del casillero
                    Text(text = "Locker Id: ${locker.lockerID}", color = Color.Black)
                    Text(text = "Dirección: ${locker.location}", color = Color.Black)
                    Text(text = "Ciudad: ${locker.city}", color = Color.Black)
                    Text(text = "Estado: ${if (locker.status) "Disponible" else "No disponible"}", color = Color.Black)
                    Text(text = "Tamaño: ${locker.size}", color = Color.Black)
                    Text(text = "Dimensiones: ${locker.dimension}", color = Color.Black)
                    Text(text = "Precio por hora: ${locker.pricePerHour}", color = Color.Black)
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono de edición
                Icon(
                    Icons.Filled.Edit,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            navController.navigate(
                                Screen.EditLocker.createRoute(
                                    user.userID,
                                    locker.lockerID
                                )
                            )
                        },
                    contentDescription = "Editar",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Icono de eliminación
                Icon(
                    Icons.Filled.Delete,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            lockerViewModel.deleteLocker(locker)
                        },
                    contentDescription = "Eliminar",
                    tint = Color.Black
                )
            }
        }
    }
}