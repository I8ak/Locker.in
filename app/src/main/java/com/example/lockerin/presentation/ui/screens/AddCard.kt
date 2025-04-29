package com.example.lockerin.presentation.ui.screens

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.R
import com.example.lockerin.domain.model.Tarjeta
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.theme.BeigeClaro
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AddCardScreen(
    userID: String,
    navController: NavHostController
) {
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("Error") }
    val cardsViewModel: CardsViewModel=viewModel()
//    val card = cardsViewModel.getCardById(userID)
    var numberCard by remember { mutableStateOf("") }
    var nameCard by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var typeCard=verificationCardType(numberCard)
    val snackbarHostState = remember { SnackbarHostState() }
    var showCustomSnackbar by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val imagen = when (typeCard) {
        "Visa" -> R.drawable.visa
        "MasterCard" -> R.drawable.mastercard
        "American Express" -> R.drawable.american_express
        else -> R.drawable.credit_card
    }
    Scaffold (
        snackbarHost= { SnackbarHost(snackbarHostState) },
        content= { padding->
            DrawerMenu(
                textoBar = "Agregar tarjeta",
                navController = navController,
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
                            onValueChange = { if(it.length<=16){
                                numberCard = it}
                            },
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
                                        color = Color.Black,
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
                                onValueChange = { newValue ->
                                    cvv = newValue.filter { it.isDigit() }
                                    if (cvv.length > 3) {
                                        cvv = cvv.substring(0..3)
                                    }
                                },
                                label = {
                                    Text(
                                        text = "CVV",
                                        color = Color.Black
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        val stringToDate= exoiryStringToDate(expirationDate)
                        Spacer(modifier = Modifier.padding(8.dp))
                        Button(
                            onClick = {

                                if (stringToDate!=null){
                                    if (verificationCard(numberCard)){
                                        val tarjeta=Tarjeta(
                                            cardID = "5",
                                            userId = userID,
                                            cardNumber = numberCard,
                                            cardName = nameCard,
                                            expDate =stringToDate,
                                            cvv=  cvv.toInt(),
                                            typeCard = typeCard
                                        )
                                        Log.d("Card",tarjeta.toString())
                                        cardsViewModel.addCard(tarjeta)
                                        showCustomSnackbar = true // Mostrar el Snackbar personalizado
                                        scope.launch {
                                            delay(2000) // Duración del Snackbar
                                            showCustomSnackbar = false
                                        }
                                        navController.popBackStack()
                                    }else{
                                        errorMessage = "Tarjeta invalida"
                                        showErrorDialog = true
                                    }
                                }else{
                                    errorMessage = "Formato de fecha de caducidad incorrecto (MM/YY)"
                                    showErrorDialog = true
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
                },
            )
            if (showCustomSnackbar) {
                CustomSnackbar(message = "Tarjeta añadida")
            }

        }
    )
    if (showErrorDialog) {
        dialog(
            onDismissRequest = { showErrorDialog = false },
            texto = errorMessage
        )
    }
}

@Composable
fun CustomSnackbar(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Card (

            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = message,
                color = White,
                modifier = Modifier.padding(12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

fun verificationCard(numberCard : String): Boolean {
    val digits = numberCard.filter { it.isDigit() }.reversed()
    var sum = 0
    for (i in digits.indices) {
        var digit = digits[i].toString().toInt()
        if (i % 2 == 1) {
            digit *= 2
            if (digit > 9) {
                digit -= 9
            }
        }
        sum += digit
    }
    return sum % 10 == 0
}

fun verificationCardType(numberCard: String): String {
    val cleanedNumber = numberCard.replace(" ", "").replace("-", "")

    if (!cleanedNumber.matches(Regex("\\d+"))) {
        return "Unknown Type (Invalid Characters)"
    }

    val length = cleanedNumber.length

    when {
        cleanedNumber.startsWith("4") && (length == 13 || length == 16) -> {
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
fun exoiryStringToDate(expirationDate: String): Date?{
    val format= SimpleDateFormat("MM/yy", Locale.getDefault())
    format.isLenient=false
    return try {
        val parsedDate=format.parse(expirationDate)
        val calendar = Calendar.getInstance()
        calendar.time = parsedDate
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val parsedTwoDigitYear = calendar.get(Calendar.YEAR) % 100
        calendar.set(Calendar.YEAR, currentYear - (currentYear % 100) + parsedTwoDigitYear)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        calendar.time

    }catch (e: Exception){
        null
    }
}

@Composable
fun dialog(
    onDismissRequest:()-> Unit,
    texto: String
){
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = BeigeClaro,
        title = {
            Text(
                text = texto,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp
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

@Preview
@Composable
fun AddCardScreenPreview(){
    AddCardScreen("1", rememberNavController())
}
