package com.example.lockerin.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    @DocumentId var id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val role: String = "",
    @ServerTimestamp var createdAt: Date? = null
) {
    constructor() : this("", "", "", "", "",null)
}