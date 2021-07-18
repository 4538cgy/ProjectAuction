package com.example.project_auction.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginSignUpViewModel(): ViewModel() {

    var profilePhotoUri = MutableLiveData<Uri>()
    var nickNameNextButtonState = MutableLiveData<Boolean>()
    var addressCheck = MutableLiveData<Boolean>()
    var nickName = MutableLiveData<String>()
    //전체 주소
    var totalAddress = MutableLiveData<String>()
    //건물 주소
    var buildingAddress = MutableLiveData<String>()
    //지번
    var address = MutableLiveData<String>()
    //상세주소
    var detailAddress = MutableLiveData<String>()


}