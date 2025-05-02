package com.example.lockerin.domain.usecase.card

import com.example.lockerin.data.source.remote.CardFirestoreRepository
import com.example.lockerin.domain.model.Tarjeta
import kotlinx.coroutines.flow.Flow

class GetCardByUserIdUseCase(
    private val cardRepository: CardFirestoreRepository
) {
    operator fun invoke(userId: String): Flow<List<Tarjeta>> {
        return cardRepository.getCardByUserId(userId)
    }
}