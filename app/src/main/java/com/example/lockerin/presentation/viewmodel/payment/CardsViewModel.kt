package com.example.lockerin.presentation.viewmodel.payment

import androidx.lifecycle.ViewModel
import com.example.lockerin.domain.model.Tarjeta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat

class CardsViewModel: ViewModel() {
    val format= SimpleDateFormat("MM/yyyy")
    private val _cards = MutableStateFlow<List<Tarjeta>>(listOf(
        Tarjeta(
            cardID = "1",
            cardNumber = "1234567890123456",
            userId = "1",
            cardName = "John Doe",
            expDate = format.parse("12/2025"),
            cvv = 123,
            typeCard = "Visa"
        ),
        Tarjeta(
            cardID = "2",
            cardNumber = "9876543210987654",
            userId = "1",
            cardName = "Jane Smith",
            expDate = format.parse("06/2028"),
            cvv = 456,
            typeCard = "MasterCard"
        ),
        Tarjeta(
            cardID = "3",
            cardNumber = "1111222233334444",
            userId = "2",
            cardName = "Alice Johnson",
            expDate = format.parse("09/2027"),
            cvv = 789,
            typeCard = "American Express"
        ),
        Tarjeta(
            cardID = "4",
            cardNumber = "5555666677778888",
            userId = "2",
            cardName = "Bob Brown",
            expDate = format.parse("03/2026"),
            cvv = 101,
            typeCard = "MasterCard"
        ),
    ))
    val cards:StateFlow<List<Tarjeta>> = _cards
    fun getCardByUserId(userId: String): Tarjeta? {
        return cards.value.find { it.userId == userId }
    }
    fun getCardById(cardId: String): Tarjeta? {
        return cards.value.find { it.cardID == cardId }
    }

    fun hasNumberCard(cardNumber: String): String {
        return "**** ${cardNumber.takeLast(4)}"
    }


}