package com.example.project_auction.repository

import com.example.project_auction.data.AlarmDTO
import com.example.project_auction.data.FcmPushDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

class AlarmRepository {

    private val db = FirebaseFirestore.getInstance()

    companion object {
        val alarmRepository = AlarmRepository()
    }

    @ExperimentalCoroutinesApi
    fun setAlarm(alarmDTO: AlarmDTO) = callbackFlow<Boolean>{
        val eventListener = db.collection("Alarm").document().set(alarmDTO).addOnCompleteListener {
            this@callbackFlow.sendBlocking(true)
        }.addOnFailureListener {
            this@callbackFlow.sendBlocking(false)
        }
        awaitClose { eventListener }
    }
}