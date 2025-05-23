package com.example.lockerin.domain.usecase.user

import com.example.lockerin.data.source.remote.UserFirestoreRepository

class EditAvatarUseCase(private val userRepository: UserFirestoreRepository) {
    suspend operator fun invoke(userID: String,avatarString: String,tipo: Int){
        userRepository.updateAvatar(userID,avatarString,tipo)
    }
}