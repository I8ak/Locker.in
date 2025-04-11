package com.example.lockerin.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore

class UserRepository (val firestore:FirebaseFirestore) {
    private val usersCollection = firestore.collection("users")


}
