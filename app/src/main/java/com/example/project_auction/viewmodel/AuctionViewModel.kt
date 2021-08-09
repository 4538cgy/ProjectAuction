package com.example.project_auction.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_auction.data.AlarmDTO
import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.data.ProductTradeDTO
import com.example.project_auction.extension.fcmPush
import com.example.project_auction.repository.AlarmRepository
import com.example.project_auction.repository.AlarmRepository.Companion.alarmRepository
import com.example.project_auction.repository.AuctionRepository
import com.example.project_auction.repository.AuctionRepository.Companion.auctionRepository
import com.example.project_auction.util.fcm.FcmPush
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuctionViewModel() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    var auctionCategory = MutableLiveData<String>()
    var joiningState = MutableLiveData<String>()

    //경매글
    var auctionData = MutableLiveData<ProductAuctionDTO.ProductResponseDTO?>()
    //거래글
    var tradeData = MutableLiveData<ProductTradeDTO.ProductResponseDTO?>()
    //내 경매글
    var myAuctionData = MutableLiveData<Map<String,ProductAuctionDTO>?>()
    //내가 참여한 경매글
    var myBiddedAUctionData = MutableLiveData<Map<String,ProductAuctionDTO>?>()
    //내 거래글
    var myTradeData = MutableLiveData<Map<String,ProductTradeDTO>?>()

    //경매글 가져오기
    fun loadAuctionData(page : Int , orderBy : Int , uid : String , sortKey : String,category : String){
        viewModelScope.launch {
            auctionRepository.loadAuctionData(page,orderBy,uid,sortKey,category).collect {
                auctionData.postValue(it)
            }
        }
    }

    //거래글 가져오기
    fun loadTradeData(page : Int , orderBy: Int , uid : String, sortKey: String , endFlag : Boolean,category : String){
        viewModelScope.launch {
            auctionRepository.loadTradeData(page , orderBy , uid , sortKey , endFlag,category).collect {
                tradeData.postValue(it)
            }
        }
    }


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

    //내 경매품 내역 가져오기
    fun getMyAuctionData(uid : String) {
        viewModelScope.launch {
            auctionRepository.getMyAuctionProduct(uid).collect {
                myAuctionData.postValue(it)
            }
        }
    }

    //내가 참여한 경매 물품 가져오기
    fun getBiddedAuctionData(uid : String){
        viewModelScope.launch {
            auctionRepository.getMyBiddedProduct(uid).collect {
                myBiddedAUctionData.postValue(it)
            }
        }
    }

    //내 판매목록 가져오기
    fun getMyTradeData(uid : String){
        viewModelScope.launch {
            auctionRepository.getMyTradeProduct(uid).collect {
                myTradeData.postValue(it)
            }
        }
    }
}