package com.example.lockerin.domain.model

import com.google.firebase.firestore.DocumentId
import java.time.temporal.TemporalAmount
import java.util.Date

data class Payment(
    @DocumentId val paymentID: String = "",
    val userID: String = "",
    val rentalID: String = "",
    val cardID: String = "",
    val amount: Double = 0.0,
    val status: Boolean= false,
    val date: Date? = null,
) {
    constructor() : this("", "", "", "", 0.0, false, null)
}