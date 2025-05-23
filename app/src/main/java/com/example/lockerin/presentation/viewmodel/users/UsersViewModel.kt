package com.example.lockerin.presentation.viewmodel.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockerin.domain.model.User
import com.example.lockerin.domain.usecase.user.DeleteUserUseCase
import com.example.lockerin.domain.usecase.user.EditAvatarUseCase
import com.example.lockerin.domain.usecase.user.GetUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.asStateFlow

class UsersViewModel(
    val getUserUseCase: GetUserUseCase,
    val deleteUserUseCase: DeleteUserUseCase,
    val editAvatarUseCase: EditAvatarUseCase
) : ViewModel() {
    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    fun getUserById(id: String) : User? {
        viewModelScope.launch {
            _user.value = getUserUseCase(id)
        }
        return _user.value
    }
    fun deleteAccount(userId: String, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                deleteUserUseCase(userId)
                onComplete(true, null)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Error desconocido"
                onComplete(false, errorMessage)
            }
        }
    }

    fun updateAvatar(userId: String, avatar: String,tipo: Int) {
        viewModelScope.launch {
            editAvatarUseCase(userId, avatar,tipo)
            getUserById(userId)
        }
    }

}