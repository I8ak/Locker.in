package com.example.lockerin.presentation.ui.screens.card

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.example.lockerin.data.utils.NetworkUtils
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lockerin.R
import com.example.lockerin.domain.model.Tarjeta
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.components.NoConexionDialog
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import org.koin.androidx.compose.koinViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddCardScreen(
    userID: String,
    navController: NavHostController,
    userViewModel: UsersViewModel = koinViewModel(),
    cardsViewModel: CardsViewModel = koinViewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    // ViewModels y estados de usuario
    val userId = authViewModel.currentUserId
    val userState by userViewModel.user.collectAsState()
    val user = userViewModel.getUserById(userID)

    // Estados de entrada del formulario
    var numberCard by remember { mutableStateOf("") }
    var nameCard by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }

    // Estados de la interfaz de usuario
    var showDialog by remember { mutableStateOf(false) }

    // Gestión del foco
    val focusManager = LocalFocusManager.current
    val numCardFocusRequest = remember { FocusRequester() }
    val nameCardFocusRequest = remember { FocusRequester() }
    val expDateCardFocusRequest = remember { FocusRequester() }
    val cvvCardFocusRequest = remember { FocusRequester() }

    // Imagen del tipo de tarjeta
    val imagen = when (verificationCardType(numberCard)) {
        "Visa" -> R.drawable.visa
        "MasterCard" -> R.drawable.mastercard
        "American Express" -> R.drawable.american_express
        else -> R.drawable.credit_card
    }

    val context= LocalContext.current
    var showDialogConection by remember { mutableStateOf(false) }
    if (showDialogConection){
        NoConexionDialog(
            onDismiss = { showDialogConection = false }
        )
    }

    LaunchedEffect(Unit) {
        userViewModel.getUserById(userID)
    }

    // Menú lateral que contiene el formulario de adición de tarjeta
    DrawerMenu(
        textoBar = "Agregar tarjeta",
        navController = navController,
        authViewModel = viewModel(),
        fullUser = userState,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Imagen del tipo de tarjeta
                Image(
                    painter = painterResource(id = imagen),
                    contentDescription = imagen.toString(),
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Start)
                )
                Spacer(modifier = Modifier.padding(8.dp))

                // Campo de entrada para el número de tarjeta
                OutlinedTextField(
                    value = numberCard,
                    onValueChange = {
                        if (it.length <= 16) numberCard = it
                    },
                    label = {
                        Text(
                            text = "Número de la tarjeta",
                            color = Color.Black
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(12.dp))
                        .focusRequester(numCardFocusRequest),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            nameCardFocusRequest.requestFocus()
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.padding(8.dp))

                // Campo de entrada para el nombre del titular de la tarjeta
                OutlinedTextField(
                    value = nameCard,
                    onValueChange = { nameCard = it },
                    label = {
                        Text(
                            text = "Nombre del titular",
                            color = Color.Black
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(12.dp))
                        .focusRequester(nameCardFocusRequest),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            expDateCardFocusRequest.requestFocus()
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.padding(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // Campo de entrada para la fecha de vencimiento
                    OutlinedTextField(
                        value = expirationDate,
                        onValueChange = {
                            if (it.length <= 5) {
                                expirationDate = it
                                isValid = isExpirationDateValid(it)
                            }
                        },
                        label = {
                            Text(
                                text = "Fecha de expiración",
                                color = Color.Black
                            )
                        },
                        placeholder = {
                            Text("MM/AA", color = Color.Gray)
                        },
                        isError = expirationDate.length == 5 && !isValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f)
                            .background(Color.Transparent, RoundedCornerShape(12.dp))
                            .focusRequester(expDateCardFocusRequest),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                cvvCardFocusRequest.requestFocus()
                            }
                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            errorBorderColor = Color.Red,
                            errorTextColor = Color.Red
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.padding(8.dp))

                    // Campo de entrada para el CVV
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { if (it.length <= 4) cvv = it },
                        label = {
                            Text(
                                text = "CVV",
                                color = Color.Black
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f)
                            .background(Color.Transparent, RoundedCornerShape(12.dp))
                            .focusRequester(cvvCardFocusRequest),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
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
                        ),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))

                // Botón para agregar tarjeta
                Button(
                    onClick = {
                        if (NetworkUtils.isInternetAvailable(context)) {
                            if (numberCard.isNotEmpty() && nameCard.isNotEmpty() && expirationDate.isNotEmpty() && cvv.isNotEmpty() && isValid) {
                                val newCard = Tarjeta(
                                    cardNumber = cardsViewModel.encrypt(numberCard),
                                    userId = userID,
                                    cardName = nameCard,
                                    expDate = expirationDate,
                                    cvv = cvv,
                                    typeCard = verificationCardType(numberCard),
                                )
                                cardsViewModel.addCard(newCard)
                                navController.popBackStack()
                            }
                        } else {
                            showDialogConection = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        disabledContentColor = White.copy(alpha = 0.7f)
                    ),
                ) {
                    Text(
                        text = "Agregar tarjeta",
                        color = White
                    )
                }
            }

            // Diálogo de tarjeta ya existente
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

// Función para verificar el tipo de tarjeta
fun verificationCardType(numberCard: String): String {
    val cleanedNumber = numberCard.replace(" ", "").replace("-", "")

    if (!cleanedNumber.matches(Regex("\\d+"))) {
        return "Tipo desconocido (caracteres inválidos)"
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

            return "Tarjeta"
        }

        (cleanedNumber.startsWith("34") || cleanedNumber.startsWith("37")) && length == 15 -> {
            return "American Express"
        }

        else -> {
            return "Tarjeta"
        }
    }
}

// Función para validar la fecha de vencimiento
@RequiresApi(Build.VERSION_CODES.O)
fun isExpirationDateValid(input: String): Boolean {
    if (!Regex("^\\d{2}/\\d{2}$").matches(input)) return false

    val (monthStr, yearStr) = input.split("/")
    val month = monthStr.toIntOrNull() ?: return false
    val year = yearStr.toIntOrNull() ?: return false

    if (month !in 1..12) return false

    val fullYear = 2000 + year

    val now = java.time.YearMonth.now()
    val inputDate = java.time.YearMonth.of(fullYear, month)

    return inputDate >= now
}