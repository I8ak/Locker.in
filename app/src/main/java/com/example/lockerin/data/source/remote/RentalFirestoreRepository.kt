package com.example.lockerin.data.source.remote

import com.example.lockerin.domain.model.Rental
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date


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
    suspend fun deleteRental(rentalId: String) {
        rentalCollection.document(rentalId).delete().await()
    }
    suspend fun isLockerAvailable(
        lockerId: String,
        newStartDate: Date,
        newEndDate: Date
    ): Boolean {
        try {
            val querySnapshot = rentalCollection
                .whereEqualTo("lockerID", lockerId)
                .get()
                .await()

            val existingRentals = querySnapshot.toObjects(Rental::class.java)

            for (existingRental in existingRentals) {
                if (areRangesOverlapping(
                        existingRental.startDate,
                        existingRental.endDate,
                        newStartDate,
                        newEndDate
                    )) {
                    return false
                }
            }

            return true

        } catch (e: Exception) {
            return false
        }
    }
    private fun areRangesOverlapping(
        start1: Date?,
        end1: Date?,
        start2: Date,
        end2: Date
    ): Boolean {
        val condition1 = if (end1 == null) {
            true
        } else {
            end1 > start2
        }

        val condition2 = if (start1 == null) {
            true
        } else {
            end2 > start1
        }

        return condition1 && condition2
    }

    suspend fun getRentalsByUserOnce(userId: String): List<Rental> {
        val snapshot = rentalCollection.whereEqualTo("userID", userId).get().await()
        return snapshot.documents.mapNotNull { doc ->
            try {
                doc.toObject(Rental::class.java)?.copy(rentalID = doc.id)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun deleteAllRentalsByUser(userId: String) {
        val rentals = getRentalsByUserOnce(userId)
        rentals.forEach { rental ->
            deleteRental(rental.rentalID)
        }
    }





}