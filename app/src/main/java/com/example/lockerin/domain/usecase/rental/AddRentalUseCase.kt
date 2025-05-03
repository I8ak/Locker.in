package com.example.lockerin.domain.usecase.rental

import com.example.lockerin.data.source.remote.RentalFirestoreRepository
import com.example.lockerin.domain.model.Rental

class AddRentalUseCase(
    private val rentalRepository: RentalFirestoreRepository
) {
    suspend operator fun invoke(rental: Rental): Unit {
        rentalRepository.save(rental)
    }
}