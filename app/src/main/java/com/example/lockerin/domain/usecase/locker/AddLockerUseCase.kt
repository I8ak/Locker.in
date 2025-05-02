package com.example.lockerin.domain.usecase.locker

import com.example.lockerin.data.source.remote.LockerFirestoreRepository
import com.example.lockerin.domain.model.Locker

class AddLockerUseCase(private val lockerRepository: LockerFirestoreRepository) {
    operator suspend fun invoke(locker: Locker): Unit {
        lockerRepository.save(locker)
    }
}