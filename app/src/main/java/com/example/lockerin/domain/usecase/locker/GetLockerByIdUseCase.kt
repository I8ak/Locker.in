package com.example.lockerin.domain.usecase.locker

import com.example.lockerin.data.source.remote.LockerFirestoreRepository
import com.example.lockerin.domain.model.Locker

class GetLockerByIdUseCase(
    private val lockerRepository: LockerFirestoreRepository
) {
    suspend operator fun invoke(lockerId: String): Locker? {
        return lockerRepository.getLockerById(lockerId)
    }
}