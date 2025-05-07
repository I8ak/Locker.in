package com.example.lockerin.domain.usecase.historicRental

import com.example.lockerin.data.source.remote.HistoricRentalFirestoreRepository
import com.example.lockerin.domain.model.HistoricRental

class AddHistoricRentalUseCase(
    private val historicRentalRepository: HistoricRentalFirestoreRepository
) {
    suspend operator fun invoke(historicRental: HistoricRental) {
        historicRentalRepository.save(historicRental)
    }
}