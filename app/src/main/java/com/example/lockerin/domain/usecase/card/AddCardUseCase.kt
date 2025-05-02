package com.example.lockerin.domain.usecase.card

import com.example.lockerin.data.source.remote.CardFirestoreRepository
import com.example.lockerin.domain.model.Tarjeta

class AddCardUseCase(
    private val addCardRepository: CardFirestoreRepository
) {
    suspend operator fun invoke(card: Tarjeta): Unit {
        addCardRepository.save(card)
    }
}