package com.example.lockerin.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class HistoricRental(
    @DocumentId val historicID: String = "",
    val userID: String = "",
    val location: String = "",
    val city: String = "",
    val size: String = "",
    val dimension: String = "",
    val cardNumber: String = "",
    val iv:String="",
    val typeCard: String="",
    val amount: Double = 0.0,
    val status: Boolean= false,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val createdAt: Date? = null
)

