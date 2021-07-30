package com.example.project_auction.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_auction.data.AlarmDTO
import com.example.project_auction.extension.fcmPush
import com.example.project_auction.repository.AlarmRepository
import com.example.project_auction.repository.AlarmRepository.Companion.alarmRepository
import com.example.project_auction.repository.AuctionRepository
import com.example.project_auction.repository.AuctionRepository.Companion.auctionRepository
import com.example.project_auction.util.fcm.FcmPush
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuctionViewModel() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()


    var auctionCategory = MutableLiveData<String>()
    var joiningState = MutableLiveData<String>()

    //경매 참여
    fun onJoinAuction(uid : String, productId : String){
        viewModelScope.launch {
            auctionRepository.joinAuction(uid , productId).collect {
                joiningState.postValue(it.toString())
            }
        }
    }

    //경매 참여 알람 보내두기
    fun onJoinAlarm(uid: String, title : String){

        var message = "경매품 : " + title + "에 신규 참여자가 있습니다."
        fcmPush(uid,11,message)
        var alarmDTO = AlarmDTO()

        alarmDTO.alarmKind = 11 //경매 참여
        alarmDTO.message = title
        alarmDTO.uid = auth.currentUser!!.uid
        alarmDTO.targetUid = uid
        alarmDTO.timestamp = System.currentTimeMillis()


        viewModelScope.launch {
            alarmRepository.setAlarm(alarmDTO).collect {

            }
        }

    }
}