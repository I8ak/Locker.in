package com.example.lockerin.data.source.remote

import com.example.lockerin.domain.model.HistoricRental
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class HistoricRentalFirestoreRepository(
    val firestore: FirebaseFirestore
) {
    private val historicRentalCollection = firestore.collection("historicRentals")
    suspend fun save(historicRental: HistoricRental) {
        val id = if (historicRental.historicID.isBlank()) {
            historicRentalCollection.document().id
        } else {
            historicRental.historicID
        }
        historicRentalCollection.document(id).set(historicRental.copy(historicID = id)).await()
    }
    fun getHRbyUserId(userId: String): Flow<List<HistoricRental>> = callbackFlow{
        val query = historicRentalCollection.whereEqualTo("userID", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val historicRentals = snapshot?.documents?.mapNotNull { doc ->
                try {
                    doc.toObject(HistoricRental::class.java)?.copy(historicID = doc.id)
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()

            trySend(historicRentals)
        }

         awaitClose { listener.remove() }
    }
    suspend fun getHistoricRentalById(historicRentalId: String): HistoricRental? {
        val document = historicRentalCollection.document(historicRentalId).get().await()
        return if (document.exists()) {
            document.toObject(HistoricRental::class.java)
        } else {
            null
        }
    }

    suspend fun editHistoricalRental(historicRental: HistoricRental){
        historicRentalCollection.document(historicRental.historicID).set(historicRental).await()
    }
}