package com.example.lockerin.domain.usecase.user

import com.example.lockerin.data.source.remote.UserFirestoreRepository
import com.example.lockerin.domain.model.User

class GetUserUseCase(
    private val userRepository: UserFirestoreRepository
) {
    operator suspend fun invoke(userId: String): User?{
        return userRepository.getUserById(userId)
    }
}