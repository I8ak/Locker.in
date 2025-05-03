package com.example.lockerin.domain.usecase.rental

import com.example.lockerin.data.source.remote.RentalFirestoreRepository
import com.example.lockerin.domain.model.Rental
import kotlinx.coroutines.flow.Flow

class ListRentalsByUserIdUseCase(
    private val rentalRepository: RentalFirestoreRepository
) {
    operator fun invoke(userId: String): Flow<List<Rental>> {
        return rentalRepository.getRentalByUserId(userId)
    }
}