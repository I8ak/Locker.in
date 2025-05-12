package com.example.lockerin.presentation.viewmodel.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockerin.domain.model.Tarjeta
import com.example.lockerin.domain.usecase.card.AddCardUseCase
import com.example.lockerin.domain.usecase.card.DeleteCardUseCase
import com.example.lockerin.domain.usecase.card.GetCardByIdUseCase
import com.example.lockerin.domain.usecase.card.GetCardByUserIdUseCase
import com.example.lockerin.domain.usecase.card.ListCardUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardsViewModel(
    listCardUseCase: ListCardUseCase,
    val getCardsUseCase: GetCardByIdUseCase,
    val addCardUseCase: AddCardUseCase,
    val deleteCardUseCase: DeleteCardUseCase,
    val getCardByUserIdUseCase: GetCardByUserIdUseCase
): ViewModel() {
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _card: MutableStateFlow<Tarjeta?> = MutableStateFlow(null)
    val cards: StateFlow<List<Tarjeta>> = _userId
        .filterNotNull()
        .flatMapLatest { userId: String ->
            getCardByUserIdUseCase(userId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getCardByUserId(userId: String): Tarjeta? {
        return cards.value.find { it.userId == userId }
    }
    fun setUserId(userId: String) {
        _userId.value = userId
    }


    fun decrypt(cardNumber: String): String {
        return "**** ${cardNumber.takeLast(4)}"
    }

    fun encrypt(cardNumber: String): String {
        return "**** ${cardNumber.takeLast(4)}"
    }
    fun addCard(card: Tarjeta) {
        viewModelScope.launch {
            addCardUseCase(card)
        }
    }
    fun removeCard(card: Tarjeta) {
        viewModelScope.launch {
            deleteCardUseCase(card)
        }
    }

    fun countCardsByUserId(userId: String): Int {
        return cards.value.count { it.userId == userId }
    }

    private val _selectedCard = MutableStateFlow<Tarjeta?>(null)
    val selectedCard: StateFlow<Tarjeta?> = _selectedCard.asStateFlow()
    fun getCardById(cardId: String) {
        viewModelScope.launch {
            val card = getCardsUseCase(cardId)
            _selectedCard.value = card
        }
    }


}