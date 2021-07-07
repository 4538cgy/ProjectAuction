package com.example.project_auction.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginSignUpViewModel(): ViewModel() {

    var profilePhotoUri = MutableLiveData<String>()
    var nickNameNextButtonState = MutableLiveData<Boolean>()
    var profileAddress = MutableLiveData<String>()
}