package com.example.lockerin.domain.usecase.locker

import com.example.lockerin.data.source.remote.LockerFirestoreRepository
import com.example.lockerin.domain.model.Locker

class EditLockerUseCase(
    private val lockerRepository: LockerFirestoreRepository
) {
    suspend operator fun invoke(locker: Locker) {
        lockerRepository.editLocker(locker)
    }
}