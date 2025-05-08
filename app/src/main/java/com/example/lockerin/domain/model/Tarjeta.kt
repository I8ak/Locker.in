package com.example.lockerin.domain.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class  Tarjeta(
    @DocumentId val cardID: String="",
    val cardNumber: String="",
    val userId: String="",
    val cardName: String="",
    val expDate: String="",
    val cvv: Int=0,
    val typeCard: String="",
    val iv: String?
) {
    constructor() : this("","", "", "", "", 0, "","")
}