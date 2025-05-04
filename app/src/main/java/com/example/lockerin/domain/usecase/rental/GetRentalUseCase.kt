package com.example.lockerin.domain.usecase.rental

import com.example.lockerin.data.source.remote.RentalFirestoreRepository
import com.example.lockerin.domain.model.Rental

class GetRentalUseCase(
    private val rentalRepository: RentalFirestoreRepository
) {
    suspend operator fun invoke(rentalID: String): Rental? {
        return rentalRepository.getRentalById(rentalID)
    }
}