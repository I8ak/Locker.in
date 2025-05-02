package com.example.lockerin.data.source.remote

import com.example.lockerin.domain.model.Tarjeta
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CardFirestoreRepository(val firestore: FirebaseFirestore) {
    private val cardCollection = firestore.collection("cards")

    suspend fun save(card: Tarjeta) {
        val id = if (card.cardID.isBlank()) {
            cardCollection.document().id
        } else {
            card.cardID
        }
        cardCollection.document(id).set(card.copy(cardID = id)).await()
    }

    suspend fun delete(card: Tarjeta) {
        cardCollection.document(card.cardID).delete().await()
    }

    suspend fun getCardById(cardId: String): Tarjeta? {
        val document = cardCollection.document(cardId).get().await()
        return if (document.exists()) {
            document.toObject(Tarjeta::class.java)
        } else {
            null
        }
    }
    fun getCardByUserId(userId: String): Flow<List<Tarjeta>> = callbackFlow {
        val query = cardCollection.whereEqualTo("userId", userId)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val tarjetas = snapshot?.documents?.mapNotNull { doc ->
                try {
                    doc.toObject(Tarjeta::class.java)?.copy(cardID = doc.id)
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()

            trySend(tarjetas)
        }

        awaitClose { listener.remove() }
    }

    fun list(): Flow<List<Tarjeta>> {
        return callbackFlow {
            val listener = cardCollection
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val items = snapshots?.documents?.mapNotNull { doc ->
                        val card = doc.toObject(Tarjeta::class.java)
                        card
                    } ?: emptyList()

                    trySend(items)
                }

            awaitClose { listener.remove() }
        }
    }
}