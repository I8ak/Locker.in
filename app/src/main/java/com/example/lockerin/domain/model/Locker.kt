package com.example.lockerin.domain.model

import com.google.firebase.firestore.DocumentId

data class Locker (
    @DocumentId val lockerID: String ="",
    val location: String = "",
    val city: String = "",
    val status: Boolean = true,
    val size: String = "",
    val dimension: String = "",
    val pricePerHour: Double = 0.0
){
//    constructor() : this("", "","", true, "", 0.0)
}