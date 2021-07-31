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

    //알람 데이터 가져오기
    @ExperimentalCoroutinesApi
    fun loadAlarmData(uid : String) = callbackFlow<ArrayList<AlarmDTO>>{
        val eventListener = db.collection("Alarm").whereEqualTo("uid",uid).get().addOnCompleteListener {
            it?.let {
                if (it.isSuccessful){
                    val datas = arrayListOf<AlarmDTO>()
                    it.result.toObjects(AlarmDTO::class.java).forEach {
                        datas.add(it)
                    }
                    this@callbackFlow.sendBlocking(datas)
                }
            }?.run {

            }
        }.addOnFailureListener {

        }

        awaitClose { eventListener }
    }

    //알람 데이터베이스에 올리기
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