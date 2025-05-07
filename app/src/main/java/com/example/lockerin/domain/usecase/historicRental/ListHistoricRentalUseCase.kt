package com.example.lockerin.domain.usecase.historicRental

import com.example.lockerin.data.source.remote.HistoricRentalFirestoreRepository
import com.example.lockerin.domain.model.HistoricRental
import kotlinx.coroutines.flow.Flow

class ListHistoricRentalUseCase(
    private val historicRentalRepository: HistoricRentalFirestoreRepository
) {
    operator fun invoke(userId: String): Flow<List<HistoricRental>> {
        return historicRentalRepository.getHRbyUserId(userId)
    }
}