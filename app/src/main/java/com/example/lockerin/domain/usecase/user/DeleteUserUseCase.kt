package com.example.lockerin.domain.usecase.user

import android.util.Log
import com.example.lockerin.data.source.remote.UserFirestoreRepository

class DeleteUserUseCase(private val userRepository: UserFirestoreRepository) {
    suspend operator fun invoke(userId: String) {
        try {
            userRepository.deleteUser(userId)
        } catch (e: Exception) {
            Log.e("Delete", "Error deleting user data for UID $userId: ${e.message}", e)
            throw e
        }
    }
}