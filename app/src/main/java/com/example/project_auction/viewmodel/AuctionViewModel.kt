package com.example.project_auction.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_auction.repository.AuctionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuctionViewModel() : ViewModel() {

    private val auctionRepository = AuctionRepository()


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
}