package com.example.project_auction.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddProductViewModel() : ViewModel() {

    //경매
    var productTitle = MutableLiveData<String>()
    var productExplain = MutableLiveData<String>()
    var productCategory = MutableLiveData<String>()
    var productPhotoList = MutableLiveData<ArrayList<String>>()
    var productAuctionTime = MutableLiveData<Long>()
    var productStartCost = MutableLiveData<Long>()
    var productLastCost = MutableLiveData<Long>()
    //거래
}