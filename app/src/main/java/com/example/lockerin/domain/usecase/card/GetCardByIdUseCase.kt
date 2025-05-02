package com.example.lockerin.domain.usecase.card

import com.example.lockerin.data.source.remote.CardFirestoreRepository
import com.example.lockerin.domain.model.Tarjeta

class GetCardByIdUseCase(
    private val cardRepository: CardFirestoreRepository
) {
    suspend operator fun invoke(cardId: String): Tarjeta? {
        return cardRepository.getCardById(cardId)
    }
}