package com.example.lockerin.domain.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Card(
    @DocumentId val cardNumber: String="",
    val userId: String="",
    val cardName: String="",
    val expDate: Date?=null,
    val cvv: Int=0,
    val typeCard: String="",
) {
    constructor() : this("", "", "", null, 0, "")
}