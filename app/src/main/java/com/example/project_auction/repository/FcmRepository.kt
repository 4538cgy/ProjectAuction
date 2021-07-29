package com.example.project_auction.repository

import com.google.firebase.firestore.FirebaseFirestore

class FcmRepository {
    private val db = FirebaseFirestore.getInstance()

    companion object{
        val fcmRepository = FcmRepository()
    }


}