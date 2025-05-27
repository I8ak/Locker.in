package com.example.lockerin.presentation.ui.screens.reserveLocker

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lockerin.R
import com.example.lockerin.data.utils.NetworkUtils
import com.example.lockerin.domain.model.Payment
import com.example.lockerin.domain.model.Rental
import com.example.lockerin.domain.model.Tarjeta
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.ui.components.LoadingScreen
import com.example.lockerin.presentation.ui.components.NoConexionDialog
import com.example.lockerin.presentation.ui.theme.Primary
import com.example.lockerin.presentation.viewmodel.lockers.LockersViewModel
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel
import com.example.lockerin.presentation.viewmodel.payment.PaymentViewModel
import com.example.lockerin.presentation.viewmodel.rentals.RentalViewModel
import com.example.lockerin.presentation.viewmodel.users.AuthViewModel
import com.example.lockerin.presentation.viewmodel.users.UsersViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.random.Random


/**
 * Composable que representa la pantalla de pago donde el usuario puede seleccionar una tarjeta
 * para pagar el alquiler de un casillero.
 *
 * @param userID El ID del usuario actual.
 * @param lockerID El ID del casillero que se va a alquilar.
 * @param startDate La fecha de inicio del alquiler en formato de cadena.
 * @param endDate La fecha de fin del alquiler en formato de cadena.
 * @param totalPrice El precio total del alquiler en formato de cadena.
 * @param navController El controlador de navegación para manejar las transiciones entre pantallas.
 * @param userViewModel ViewModel para la gestión de datos del usuario.
 * @param lockersViewModel ViewModel para la gestión de datos de los casilleros.
 * @param paymentViewModel ViewModel para la gestión de datos de pagos.
 * @param cardsViewModel ViewModel para la gestión de datos de tarjetas.
 * @param rentalViewModel ViewModel para la gestión de datos de alquileres.
 * @param authViewModel ViewModel para la autenticación de usuarios.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentScreen(
    userID: String,
    lockerID: String,
    startDate: String,
    endDate: String,
    totalPrice: String,
    navController: NavHostController = rememberNavController(),
    userViewModel: UsersViewModel = koinViewModel(),
    lockersViewModel: LockersViewModel = koinViewModel(),
    paymentViewModel: PaymentViewModel = koinViewModel(),
    cardsViewModel: CardsViewModel = koinViewModel(),
    rentalViewModel: RentalViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
) {
    // Obtiene el ID del usuario actual del AuthViewModel.
    val userId = authViewModel.currentUserId
    // Recopila el estado del usuario.
    val userState by userViewModel.user.collectAsState()
    // Recopila la lista de tarjetas del usuario.
    val cardsState by cardsViewModel.cards.collectAsState()

    // Estado para controlar la visualización de la pantalla de carga.
    var isLoading by remember { mutableStateOf(true) }
    // Estado para almacenar el ID de la tarjeta seleccionada.
    var selectedCardId by remember { mutableStateOf<String?>(null) }

    // Efecto que se lanza una vez que el Composable entra en la composición.
    LaunchedEffect(userID) {
        cardsViewModel.setUserId(userID)
        delay(1000)
        isLoading = false
    }

    val context = LocalContext.current
    var showDialogConection by remember { mutableStateOf(false) }
    if (showDialogConection) {
        NoConexionDialog(
            onDismiss = { showDialogConection = false }
        )
    }

    // Muestra la pantalla de carga si isLoading es verdadero.
    if (isLoading) {
        LoadingScreen(isLoading)
    } else {
        DrawerMenu(
            textoBar = "Medios de pago",
            navController = navController,
            authViewModel = viewModel(),
            fullUser = userState,
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Título de la sección de selección de tarjeta.
                    Text(
                        text = "Selecciona una tarjeta",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.padding(16.dp))

                    // Lista desplazable de tarjetas del usuario.
                    LazyColumn(
                        modifier = Modifier
                            .background(Color.Transparent)
                    ) {
                        items(cardsState) { card ->
                            key(card.cardID) {
                                Log.d("Card", "Tarjeta: ${card.cardID}")
                                CardsCard(
                                    tarjeta = card,
                                    isSelected = card.cardID == selectedCardId,
                                    onCardSelected = { selectedCardId = it },
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    // Botón para añadir una nueva tarjeta.
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp)
                            .clickable {
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    navController.navigate(Screen.AddCard.createRoute(userID))
                                } else {
                                    showDialogConection = true
                                }
                            },
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
                        )
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    // Sección que muestra el precio total.
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Precio total",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp)
                                .weight(1f)
                                .height(30.dp),
                            fontSize = 20.sp,
                            color = Color.Black,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$totalPrice €",
                            modifier = Modifier
                                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp)
                                .weight(1f)
                                .height(30.dp),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.padding(16.dp))
                    // Botones de "Pagar" y "Cancelar".
                    Row {
                        Button(
                            // El botón de pagar está habilitado solo si se ha seleccionado una tarjeta.
                            enabled = selectedCardId != null,
                            onClick = {
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    // Genera un ID de alquiler aleatorio.
                                    val rentalIDRandom = generarNumeroSeisDigitos().toString()
                                    val currebntCardId = selectedCardId
                                    // Crea un objeto Rental con los datos del alquiler.
                                    val rental = Rental(
                                        rentalID = rentalIDRandom,
                                        userID = userID,
                                        lockerID = lockerID,
                                        startDate = transformDate(startDate),
                                        endDate = transformDate(endDate),
                                    )
                                    // Genera un nuevo ID de pago.
                                    val paymentID =
                                        FirebaseFirestore.getInstance().collection("payments")
                                            .document().id
                                    // Crea un objeto Payment con los detalles del pago.
                                    val payment = Payment(
                                        paymentID = paymentID,
                                        userID = userID,
                                        rentalID = rental.rentalID,
                                        cardID = currebntCardId.toString(),
                                        amount = totalPrice.toDouble(),
                                        status = true,
                                    )
                                    // Si el pago es exitoso, actualiza el estado del casillero y añade el alquiler.
                                    if (payment.status == true) {
                                        lockersViewModel.setStatus(lockerID, false)
                                        rentalViewModel.addRental(rental)
                                    }
                                    // Añade el registro de pago.
                                    paymentViewModel.addPayment(payment)

                                    // Logs para depuración sobre los objetos creados.
                                    Log.d("Rental", "Alquiler agregado: $rental")
                                    Log.d("Payment", "Pago agregado: $payment")

                                    // Navega a la pantalla de estado de pago, pasando los IDs relevantes.
                                    navController.navigate(
                                        Screen.StatusPay.createRoute(
                                            userID,
                                            currebntCardId.toString(),
                                            payment.paymentID, rentalIDRandom
                                        )
                                    ) {
                                        popUpTo(Screen.Payment.route) {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    showDialogConection = true
                                }

                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary,
                                disabledContainerColor = Color.Gray
                            ),
                            modifier = Modifier.width(130.dp)
                        ) {
                            Text(text = "Pagar", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    navController.navigate(Screen.Home.route)
                                } else {
                                    showDialogConection = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            modifier = Modifier.width(130.dp)
                        ) {
                            Text(
                                text = "Cancelar",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        )
    }
}


fun generarNumeroSeisDigitos(): Int {
    return Random.nextInt(100000, 1000000)
}


@RequiresApi(Build.VERSION_CODES.O)
fun transformDate(date: String): Date {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    val localDateTime = LocalDateTime.parse(date, formatter)
    val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
    return Date.from(instant)
}


@Composable
fun CardsCard(
    tarjeta: Tarjeta,
    isSelected: Boolean,
    onCardSelected: (cardId: String) -> Unit,
) {
    // Determina la imagen de la tarjeta basándose en el tipo de tarjeta.
    val imagen = when (tarjeta.typeCard) {
        "Visa" -> R.drawable.visa
        "MasterCard" -> R.drawable.mastercard
        "American Express" -> R.drawable.american_express
        else -> R.drawable.credit_card
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
                // Botón de radio para seleccionar la tarjeta.
                RadioButton(
                    selected = isSelected,
                    onClick = { onCardSelected(tarjeta.cardID) },
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Gray
                    )
                )
            }
        }
    }
}