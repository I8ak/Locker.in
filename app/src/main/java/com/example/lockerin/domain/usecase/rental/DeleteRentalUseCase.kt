package com.example.lockerin.domain.usecase.rental

import com.example.lockerin.data.source.remote.RentalFirestoreRepository
import com.example.lockerin.domain.model.Rental

class DeleteRentalUseCase(
    private val rentalRepository: RentalFirestoreRepository
) {
    suspend operator fun invoke(rental: Rental) {
        rentalRepository.deleteRental(rental.toString())
    }
}