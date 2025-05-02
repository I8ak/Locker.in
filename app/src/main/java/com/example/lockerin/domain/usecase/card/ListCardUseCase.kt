package com.example.lockerin.domain.usecase.card

import com.example.lockerin.data.source.remote.CardFirestoreRepository
import com.example.lockerin.domain.model.Tarjeta
import kotlinx.coroutines.flow.Flow

class ListCardUseCase(val listCardRepository: CardFirestoreRepository) {
    operator fun invoke(): Flow<List<Tarjeta>> {
        return listCardRepository.list()
    }
}