package com.example.lockerin.domain.usecase.locker

import com.example.lockerin.data.source.remote.LockerFirestoreRepository
import com.example.lockerin.domain.model.Locker

class AddPuntuacionUseCase(
    private val lockerRepository: LockerFirestoreRepository
) {
    operator suspend fun invoke(lockerId: String,nuevaPuntuacion: Float): Unit {
        lockerRepository.guardarPuntuacionEnFirestore(lockerId, nuevaPuntuacion)
    }
}