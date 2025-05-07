package com.example.lockerin.domain.usecase.rental

import com.example.lockerin.data.source.remote.RentalFirestoreRepository

class CountRentalsByUserUseCase(
    private val rentalRepository: RentalFirestoreRepository
) {
    suspend operator fun invoke(userId: String): Int {
        return rentalRepository.countRentalsById(userId)
    }}