package com.example.lockerin.domain.usecase.rental

import com.example.lockerin.data.source.remote.RentalFirestoreRepository

import java.util.Date

class IsLockerAvailableUseCase(
    private val rentalFirestoreRepository: RentalFirestoreRepository,
) {
    suspend operator fun invoke(
        lockerId: String,
        newStartDate: Date,
        newEndDate: Date): Boolean{
        return rentalFirestoreRepository.isLockerAvailable(lockerId, newStartDate, newEndDate)
    }
}