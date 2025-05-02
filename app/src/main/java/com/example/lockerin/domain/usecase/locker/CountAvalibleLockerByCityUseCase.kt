package com.example.lockerin.domain.usecase.locker

import com.example.lockerin.data.source.remote.LockerFirestoreRepository

class CountAvalibleLockerByCityUseCase(
    private val lockerRepository: LockerFirestoreRepository
) {
    suspend operator fun invoke(city: String): Int {
        return lockerRepository.countAvailableLockersByCity(city)
    }
}