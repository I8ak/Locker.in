package com.example.lockerin.domain.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class HistoricRental(
    @DocumentId val historicID: String = "",
    val userID: String = "",
    val rentalID: String = "",
    val lockerID: String = "",
    val paymentID: String = "",
    val startDate: Date? = null,
    val endDate: Date? = null,
){
    constructor() : this("", "", "", "", "", null, null)
}
