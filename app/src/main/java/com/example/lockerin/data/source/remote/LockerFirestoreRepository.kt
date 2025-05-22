package com.example.lockerin.data.source.remote

import android.util.Log
import com.example.lockerin.domain.model.Locker
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.math.RoundingMode
import java.sql.Date

class LockerFirestoreRepository(val firestore: FirebaseFirestore) {
    private val lockerCollection = firestore.collection("lockers")

    suspend fun getLockerById(lockerId: String): Locker? {
        return lockerCollection.document(lockerId).get().await().toObject(Locker::class.java)
    }

    suspend fun editLocker(locker: Locker) {
        lockerCollection.document(locker.lockerID).set(locker).await()
    }
    suspend fun deleteLocker(locker: Locker) {
        lockerCollection.document(locker.lockerID).delete().await()
    }
    fun list(): Flow<List<Locker>> {
        return callbackFlow {
            val listener = lockerCollection
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val items = snapshots?.documents?.mapNotNull { doc ->
                        val locker = doc.toObject(Locker::class.java)
                        locker
                    } ?: emptyList()

                    trySend(items)
                }

            awaitClose { listener.remove() }
        }
    }

    suspend fun save(locker: Locker) {
        lockerCollection.document(locker.lockerID).set(locker).await()
    }

    suspend fun countAvailableLockersByCity(city: String): Int {
        val snapshot = firestore.collection("lockers")
            .whereEqualTo("city", city)
            .get()
            .await()

        return snapshot.size()
    }

    suspend fun isLockerAvailable(lockerId: String,city: String): Boolean {
        val snapshot= firestore.collection("lockers")
            .whereEqualTo("status", true)
            .whereEqualTo("lockerID", lockerId)
            .whereEqualTo("city", city)
            .get()
            .await()
        return snapshot.size() > 0
    }

    fun guardarPuntuacionEnFirestore(lockerId: String, nuevaPuntuacion: Float) {
        val firestore = FirebaseFirestore.getInstance()
        val lockerRef = firestore.collection("lockers").document(lockerId)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(lockerRef)

            val puntuacionActual = snapshot.getDouble("puntuacion") ?: 0.0
            val numeroActual = snapshot.getLong("numValoraciones") ?: 0

            val totalPuntos = puntuacionActual * numeroActual
            val nuevoTotal = totalPuntos + nuevaPuntuacion
            val nuevoNumero = numeroActual + 1

            val nuevaMedia = nuevoTotal / nuevoNumero

            val mediaToDouble = nuevaMedia.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()

            transaction.update(lockerRef, mapOf(
                "puntuacion" to mediaToDouble,
                "numValoraciones" to nuevoNumero
            ))
        }.addOnSuccessListener {
            Log.d("Rating", "Puntuación guardada con éxito")
        }.addOnFailureListener {
            Log.e("Rating", "Error al guardar puntuación", it)
        }
    }


}