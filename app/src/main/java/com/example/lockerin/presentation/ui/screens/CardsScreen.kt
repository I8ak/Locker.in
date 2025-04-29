package com.example.lockerin.presentation.ui.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
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
import com.example.lockerin.domain.model.Tarjeta
import com.example.lockerin.presentation.navigation.Screen
import com.example.lockerin.presentation.ui.components.DrawerMenu
import com.example.lockerin.presentation.viewmodel.payment.CardsViewModel

@Composable
fun CardsScreen(
    userID: String,
    navController: NavHostController = rememberNavController(),
) {
    val cardsViewModel: CardsViewModel = viewModel()
    val cardsState = cardsViewModel.cards.collectAsState()
    DrawerMenu(
        textoBar = "Mis tarjetas",
        navController = navController,
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
                        cardsState.value.filter { it.userId == userID }
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
    cardsViewModel: CardsViewModel
) {
    val imagen = when (tarjeta.typeCard) {
        "Visa" -> R.drawable.visa
        "MasterCard" -> R.drawable.mastercard
        "American Express" -> R.drawable.american_express
        else -> R.drawable.credit_card
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
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
                    text = cardsViewModel.hasNumberCard(tarjeta.cardNumber), color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCardsScreen() {
    CardsScreen(userID = "1")
}