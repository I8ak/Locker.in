package com.example.lockerin.presentation.viewmodel.users

import androidx.lifecycle.ViewModel
import com.example.lockerin.domain.model.Locker
import com.example.lockerin.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

class UsersViewModel: ViewModel() {
    private val _users = MutableStateFlow<List<User>>(
        listOf(
            User(
                userID = "1",
                name = "John Doe",
                email = "william@example.com",
                password = "password123",
                role = "user",
                createdAt = Date()
            ),
//            User(
//                id = "2",
//                name = "Jane Smith",
//                email = "jane@example.com",
//                password = "securePass456",
//                role = "admin",
//                createdAt = Date()
//            ),
//            User(
//                id = "3",
//                name = "Alice Johnson",
//                email = "alice@example.com",
//                password = "alicePass789",
//                role = "user",
//                createdAt =  Date()  // Fecha actual
//            ),
//            User(
//                id = "4",
//                name = "Bob Brown",
//                email = "bob@example.com",
//                password = "bobPass2024",
//                role = "manager",
//                createdAt = Date()
//            ),
//            User(
//                id = "5",
//                name = "Emily Davis",
//                email = "emily@example.com",
//                password = "emily123!",
//                role = "user",
//                createdAt = Date()
//            )
        )
    )
    val users: StateFlow<List<User>> = _users
    fun getUserById(userID: String): User? {
        return users.value.find { it.userID == userID }
    }
}