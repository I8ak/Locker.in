package com.example.lockerin.data.source.remote

import android.util.Log
import com.example.lockerin.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserFirestoreRepository(firestore: FirebaseFirestore) {
    private val userCollection = firestore.collection("users")

    suspend fun getUserById(id: String): User? {
        return try {
            val documentSnapshot = userCollection.document(id).get().await()
            documentSnapshot.toObject(User::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    suspend fun deleteUser(userId: String) {
        try {
            userCollection.document(userId).delete().await()
        } catch (e: Exception) {
            Log.e("Delete", "Error deleting user document for UID $userId: ${e.message}", e)
            throw e
        }
    }

    suspend fun updateAvatar(userId: String, avatar: String, tipo: Int) {
        try {
            userCollection.document(userId).update(
                mapOf(
                    "avatar" to avatar,
                    "tipo" to tipo
                )
            ).await()
        } catch (e: Exception) {
            Log.e("UpdateAvatar", "Error updating avatar for UID $userId: ${e.message}", e)
            throw e
        }
    }

}