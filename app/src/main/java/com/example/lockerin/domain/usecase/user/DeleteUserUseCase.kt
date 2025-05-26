package com.example.lockerin.domain.usecase.user

import android.util.Log
import com.example.lockerin.data.source.remote.CardFirestoreRepository
import com.example.lockerin.data.source.remote.RentalFirestoreRepository
import com.example.lockerin.data.source.remote.UserFirestoreRepository

class DeleteUserUseCase(
    private val userRepository: UserFirestoreRepository,
    private val rentalFirestoreRepository: RentalFirestoreRepository,
    private val cardFirestoreRepository: CardFirestoreRepository
) {
    suspend operator fun invoke(userId: String) {
        rentalFirestoreRepository.deleteAllRentalsByUser(userId)
        cardFirestoreRepository.deleteAllCardsByUser(userId)
        userRepository.deleteUser(userId)
    }
}