package com.example.lockerin.domain.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Rental(
    @DocumentId val rentalID: String = "",
    val userID: String = "",
    val lockerID: String = "",
    val startDate: Date? = null,
    val endDate: Date? = null,
    val duration: Int = 0
){
    constructor() : this("", "", "", null, null, 0)
}
