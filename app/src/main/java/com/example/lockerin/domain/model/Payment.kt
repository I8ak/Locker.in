package com.example.lockerin.domain.model

import com.google.firebase.firestore.DocumentId
import java.time.temporal.TemporalAmount

data class Payment(
    @DocumentId val paymentID: String = "",
    val userID: String = "",
    val rentalID: String = "",
    val cardNumber: String = "",
    val amount: Double = 0.0,
    val paymentMethod: String = "",
    val status: String = "",
    val date: String = "",
) {
    constructor() : this("", "", "", "", 0.0, "", "", "")
}