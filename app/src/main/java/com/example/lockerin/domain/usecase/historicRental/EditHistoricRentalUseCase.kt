package com.example.lockerin.domain.usecase.historicRental

import com.example.lockerin.data.source.remote.HistoricRentalFirestoreRepository
import com.example.lockerin.domain.model.HistoricRental

class EditHistoricRentalUseCase(
    private val historicRentalFirestoreRepository: HistoricRentalFirestoreRepository
) {
    suspend operator fun invoke(historicRental: HistoricRental){
        historicRentalFirestoreRepository.editHistoricalRental(historicRental)
    }
}