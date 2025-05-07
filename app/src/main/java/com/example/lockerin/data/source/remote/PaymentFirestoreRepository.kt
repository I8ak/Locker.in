package com.example.lockerin.data.source.remote

import com.example.lockerin.domain.model.Payment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PaymentFirestoreRepository(
    private val firestore: FirebaseFirestore
) {
    private val paymentCollection = firestore.collection("payments")

    suspend fun getPaymentById(paymentId: String): Payment? {
        val document = paymentCollection.document(paymentId).get().await()
        return if (document.exists()) {
            document.toObject(Payment::class.java)?.copy(paymentID = document.id)
        } else {
            null
        }
    }



    suspend fun save(payment: Payment) {
        val id = if (payment.paymentID.isBlank()) {
            paymentCollection.document().id
        } else {
            payment.paymentID
        }
        paymentCollection.document(id).set(payment.copy(paymentID = id)).await()
    }

    fun getPaymentByUserId(userId: String): Flow<List<Payment>> = callbackFlow {
        val query = paymentCollection.whereEqualTo("userID", userId)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val payments = snapshot?.documents?.mapNotNull { doc ->
                try {
                    doc.toObject(Payment::class.java)?.copy(paymentID = doc.id)
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()

            trySend(payments)
        }

        awaitClose { listener.remove() }
    }
}