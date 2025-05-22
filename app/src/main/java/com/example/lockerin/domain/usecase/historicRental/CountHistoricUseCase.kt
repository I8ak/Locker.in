package com.example.lockerin.domain.usecase.historicRental

import com.example.lockerin.data.source.remote.HistoricRentalFirestoreRepository

class CountHistoricUseCase(
    private val historicRentalFirestoreRepository: HistoricRentalFirestoreRepository
) {
    suspend operator fun invoke(userID:String, status: Boolean):Int{
        return historicRentalFirestoreRepository.countHistoricRentals(userID,status)
    }
}