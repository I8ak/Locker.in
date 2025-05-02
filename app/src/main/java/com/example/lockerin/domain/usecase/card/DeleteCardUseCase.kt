package com.example.lockerin.domain.usecase.card

import com.example.lockerin.data.source.remote.CardFirestoreRepository
import com.example.lockerin.domain.model.Tarjeta

class DeleteCardUseCase(
    private val cardRepository: CardFirestoreRepository
) {
    operator suspend fun invoke(card: Tarjeta) {
        cardRepository.delete(card)
    }
}