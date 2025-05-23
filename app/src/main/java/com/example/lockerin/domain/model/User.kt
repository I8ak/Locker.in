package com.example.lockerin.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    @DocumentId var userID: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val avatar: String = "",
    val tipo: Int = 0,
    @ServerTimestamp var createdAt: Date? = null
) {
    constructor() : this("", "", "", "","",0,null)
}