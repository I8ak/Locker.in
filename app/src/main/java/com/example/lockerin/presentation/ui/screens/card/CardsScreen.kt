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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.R
import com.example.lockerin.domain.model.Tarjeta
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.components.decrypt
import com.example.lockerin.presentation.ui.components.generateAesKey
import com.example.lockerin.presentation.ui.screens.user.ConfirmDeleteAccountDialog
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import org.koin.androidx.compose.koinViewModel
import javax.crypto.SecretKey

@Composable
fun CardsScreen(
    userID: String,
    navController: NavHostController = rememberNavController(),
    userViewModel: UsersViewModel= koinViewModel(),
    cardsViewModel: CardsViewModel = koinViewModel()
) {
    val user by userViewModel.user.collectAsState()
    val cardsState by cardsViewModel.cards.collectAsState()

    LaunchedEffect(userID) {
        cardsViewModel.setUserId(userID)
    }

    DrawerMenu(
        textoBar = "Mis tarjetas",
        navController = navController,
        authViewModel = viewModel(),
        fullUser = user,
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(paddingValues)
                    .padding(vertical = 26.dp, horizontal = 16.dp)
                    .fillMaxSize(),
            ) {
                LazyColumn{
                    items(
                        cardsState
                    ) { card ->
                        key(card.cardID) {
                            CardsCard(
                                tarjeta = card,
                                cardsViewModel = cardsViewModel
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp),
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
                        tint = Color.Black,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.AddCard.createRoute(userID))
                        }
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

    var isSelected by remember { mutableStateOf(false) }
    val imagen = when (tarjeta.typeCard) {
        "Visa" -> R.drawable.visa
        "MasterCard" -> R.drawable.mastercard
        "American Express" -> R.drawable.american_express
        else -> R.drawable.credit_card
    }
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
        .clickable { isSelected = !isSelected },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = tarjeta.typeCard,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = imagen),
                    contentDescription = tarjeta.typeCard,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(60.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = tarjeta.cardNumber, color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
            }
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
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            showDialog=true
                        }
                        .align(Alignment.End),
                    tint = Color.Black,
                )
                if (showDialog) {
                    ConfirmDeleteCardDialog(
                        onConfirmation = {
                            cardsViewModel.removeCard(tarjeta)
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
                text = "¿Quieres eliminar tu tajeta de forma permanente?",
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
