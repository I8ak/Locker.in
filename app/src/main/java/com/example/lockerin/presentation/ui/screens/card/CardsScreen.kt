package com.example.lockerin.presentation.ui.screens.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.R
import com.example.lockerin.domain.model.Tarjeta
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CardsScreen(
    userID: String,
    navController: NavHostController = rememberNavController(),
    userViewModel: UsersViewModel = koinViewModel(),
    cardsViewModel: CardsViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val userId = authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()
    val user = userViewModel.getUserById(userId.toString())
    val cardsState by cardsViewModel.cards.collectAsState()

    // Efecto para establecer el ID del usuario en el ViewModel de tarjetas cuando la pantalla se lanza
    LaunchedEffect(userId.toString()) {
        cardsViewModel.setUserId(userId.toString())
    }

    // Componente de menú lateral que envuelve el contenido de la pantalla
    DrawerMenu(
        textoBar = "Mis tarjetas",
        navController = navController,
        authViewModel = viewModel(),
        fullUser = userState,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(paddingValues)
                    .padding(vertical = 26.dp, horizontal = 16.dp)
                    .fillMaxSize(),
            ) {
                // Lista de tarjetas del usuario
                LazyColumn {
                    items(cardsState) { card ->
                        key(card.cardID) {
                            CardsCard(
                                tarjeta = card,
                                cardsViewModel = cardsViewModel
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Fila para el botón "Añadir una tarjeta nueva"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(Screen.AddCard.createRoute(userId.toString()))
                        }
                ) {
                    Text(
                        text = "Añadir una tarjeta nueva",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.AddCard,
                        contentDescription = "AddCard",
                        tint = Color.Black
                    )
                }
            }
        }
    )
}

@Composable
fun CardsCard(
    tarjeta: Tarjeta,
    cardsViewModel: CardsViewModel,
) {
    // Estado para controlar si la tarjeta está seleccionada/expandida
    var isSelected by remember { mutableStateOf(false) }
    // Determina la imagen de la tarjeta según su tipo
    val imagen = when (tarjeta.typeCard) {
        "Visa" -> R.drawable.visa
        "MasterCard" -> R.drawable.mastercard
        "American Express" -> R.drawable.american_express
        else -> R.drawable.credit_card // Imagen por defecto
    }
    // Estado para controlar la visibilidad del diálogo de confirmación de eliminación
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .clickable { isSelected = !isSelected },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Muestra el tipo de tarjeta (Visa, MasterCard, etc.)
            Text(
                text = tarjeta.typeCard,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                // Muestra la imagen de la tarjeta
                Image(
                    painter = painterResource(id = imagen),
                    contentDescription = tarjeta.typeCard,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(60.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                // Muestra el número de tarjeta
                Text(
                    text = tarjeta.cardNumber, color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
            }
            // Si la tarjeta está seleccionada, muestra detalles adicionales y el icono de eliminar
            if (isSelected) {
                Text(
                    text = "Nombre: ${tarjeta.cardName}",
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = "Fecha de vencimiento: ${tarjeta.expDate}",
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                // Icono para eliminar la tarjeta
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            showDialog = true
                        }
                        .align(Alignment.End),
                    tint = Color.Black,
                )
                // Si showDialog es verdadero, muestra el diálogo de confirmación
                if (showDialog) {
                    ConfirmDeleteCardDialog(
                        onConfirmation = {
                            cardsViewModel.removeCard(tarjeta)
                            showDialog = false
                        },
                        onDismissRequest = {
                            showDialog = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteCardDialog(
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = BeigeClaro,
        title = {
            Text(
                text = "¿Eliminar Tarjeta?",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                color = Color.Black,
            )
        },
        text = {
            Text(
                text = "¿Quieres eliminar tu tarjeta de forma permanente?",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                color = Color.Black,
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón "Sí" para confirmar la eliminación
                Button(
                    onClick = {
                        onConfirmation()
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Sí", color = White)
                }
                // Botón "No" para cancelar la eliminación
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("No", color = White)
                }
            }
        },
        dismissButton = null
    )
}