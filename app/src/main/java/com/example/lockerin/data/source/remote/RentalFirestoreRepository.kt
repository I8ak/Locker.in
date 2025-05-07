package com.example.lockerin.data.source.remote

import com.example.lockerin.domain.model.Rental
import com.example.lockerin.domain.model.Tarjeta
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RentalFirestoreRepository(private val firestore: FirebaseFirestore) {
    private val rentalCollection = firestore.collection("rentals")

    suspend fun getRentalById(rentalId: String): Rental? {
        val document = rentalCollection.document(rentalId).get().await()
        return if (document.exists()) {
            document.toObject(Rental::class.java)?.copy(rentalID = document.id)
        } else {
            null
        }
    }


    suspend fun save(rental: Rental) {
        val id = if (rental.rentalID.isBlank()) {
            rentalCollection.document().id
        } else {
            rental.rentalID
        }
        rentalCollection.document(id).set(rental.copy(rentalID =  id)).await()
    }

    fun getRentalByUserId(userId: String): Flow<List<Rental>> = callbackFlow {
        val query = rentalCollection.whereEqualTo("userID", userId)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val rentals = snapshot?.documents?.mapNotNull { doc ->
                try {
                    doc.toObject(Rental::class.java)?.copy(rentalID = doc.id)
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()

            trySend(rentals)
        }

        awaitClose { listener.remove() }
    }
    suspend fun countRentalsById(userId: String): Int {
        val snapshot = rentalCollection
            .whereEqualTo("userID", userId)
            .get()
            .await()

        return snapshot.size()
    }
    suspend fun deleteRental(rental: Rental) {
        rentalCollection.document(rental.rentalID).delete().await()
    }
    
    
}