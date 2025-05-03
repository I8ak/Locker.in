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

    fun getRentalById(rentalID: String) = rentalCollection.document(rentalID).get()


    suspend fun save(rental: Rental) {
        val id = if (rental.rentalID.isBlank()) {
            rentalCollection.document().id
        } else {
            rental.rentalID
        }
        rentalCollection.document(id).set(rental.copy(rentalID =  id)).await()
    }

    fun getRentalByUserId(userId: String): Flow<List<Rental>> = callbackFlow {
        val query = rentalCollection.whereEqualTo("userId", userId)

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
    
    
}