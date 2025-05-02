package com.example.lockerin.domain.usecase.locker

import com.example.lockerin.data.source.remote.LockerFirestoreRepository
import com.example.lockerin.domain.model.Locker
import kotlinx.coroutines.flow.Flow

class ListLockersUseCase(val repository: LockerFirestoreRepository) {
    operator fun invoke(): Flow<List<Locker>> {
        return repository.list()
    }
}