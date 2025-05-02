package com.example.lockerin.presentation.ui.screens.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
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
import com.example.lockerin.presentation.ui.screens.user.UserConfirmationDialog
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar
import java.util.Date

@Composable
fun AddCardScreen(
    userID: String,
    navController: NavHostController,
    userViewModel: UsersViewModel= koinViewModel(),
    cardsViewModel: CardsViewModel=koinViewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        userViewModel.getUserById(userID)
    }
    val user=userViewModel.getUserById(userID)
    var numberCard by remember { mutableStateOf("") }
    var nameCard by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    val imagen = when (verificationCardType(numberCard)) {
        "Visa" -> R.drawable.visa
        "MasterCard" -> R.drawable.mastercard
        "American Express" -> R.drawable.american_express
        else -> R.drawable.credit_card
    }
    var showDialog by remember { mutableStateOf(false) }

    DrawerMenu(
        textoBar = "Agregar tarjeta",
        navController = navController,
        authViewModel = viewModel(),
        fullUser = user,
        content = { paddingValues ->

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Image(
                    painter = painterResource(id = imagen),
                    contentDescription = imagen.toString(),
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Start)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    value = numberCard,
                    onValueChange = { numberCard = it },
                    label = {
                        Text(
                            text = "Numero de la tarjeta",
                            color = Color.Black
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors =  OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    value = nameCard,
                    onValueChange = { nameCard = it },
                    label = {
                        Text(
                            text = "Nombre de la tarjeta",
                            color = Color.Black
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors =  OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    OutlinedTextField(
                        value = expirationDate,
                        onValueChange = { expirationDate = it },
                        label = {
                            Text(
                                text = "Fecha expiración",
                                color = Color.Black
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f)
                            .background(Color.Transparent, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors =  OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { cvv = it },
                        label = {
                            Text(
                                text = "CVV",
                                color = Color.Black
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f)
                            .background(Color.Transparent, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors =  OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Button(
                    onClick = {
                        if (numberCard.isNotEmpty() && nameCard.isNotEmpty() && expirationDate.isNotEmpty() && cvv.isNotEmpty()) {
                            val newCard= Tarjeta(
                                cardNumber = numberCard,
                                userId = userID,
                                cardName = nameCard,
                                expDate = expirationDate,
                                cvv = cvv.toInt(),
                                typeCard = verificationCardType(numberCard)
                            )
                            cardsViewModel.addCard(
                                newCard
                            )
                            navController.navigate(Screen.Cards.createRoute(userID))
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        disabledContentColor = Color.White.copy(alpha = 0.7f)
                    ),
                ) {
                    Text(
                        text = "Agregar tarjeta",
                        color = Color.White
                    )
                }
                
            }
            if (showDialog) {
                CardDialog(
                    onDismissRequest = {
                        showDialog = false
                    }
                )
            }
        }
    )
}


@Composable
fun CardDialog(
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = BeigeClaro,
        title = {
            Text(
                text = "La tarjeta ya existe",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp,
                color = Color.Black
            )
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("Cerrar", color = White)
            }
        },
        dismissButton = null
    )
}



fun verificationCardType(numberCard: String): String {
    val cleanedNumber = numberCard.replace(" ", "").replace("-", "")

    if (!cleanedNumber.matches(Regex("\\d+"))) {
        return "Unknown Type (Invalid Characters)"
    }

    val length = cleanedNumber.length

    when {
        cleanedNumber.startsWith("4") && (length == 13 || length == 16 || length == 19) -> {
            return "Visa"
        }

        length == 16 -> {
            val prefix2 = cleanedNumber.take(2)
            val prefix4 = cleanedNumber.take(4)

            val prefix2Int = prefix2.toIntOrNull()
            val prefix4Int = prefix4.toIntOrNull()

            if (prefix2Int != null && prefix2Int in 51..55) {
                return "MasterCard"
            }

            if (prefix4Int != null && prefix4Int in 2221..2720) {
                return "MasterCard"
            }

            return "Unknown Type"
        }

        (cleanedNumber.startsWith("34") || cleanedNumber.startsWith("37")) && length == 15 -> {
            return "American Express"
        }

        else -> {
            return "Unknown Type"
        }
    }

}

@Preview
@Composable
fun AddCardScreenPreview(){
    AddCardScreen("1", rememberNavController())
}
