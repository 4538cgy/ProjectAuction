package com.example.project_auction.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_auction.data.UserDTO
import com.example.project_auction.repository.ProfileRepository.Companion.profileRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    var profileImage = MutableLiveData<Uri?>()
    var nickName = MutableLiveData<String?>()

    var profileUploadCheck = MutableLiveData<Boolean>()
    var userDataSetUploadCheck = MutableLiveData<Boolean>()

    //유저 닉네임 얻어오기
    fun getUserNickName(uid : String){
        viewModelScope.launch {
            profileRepository.getNickName(uid).collect {
                nickName.postValue(it)
            }
        }

    }

    //유저 프로필 이미지 uri 값 얻어오기
    fun getProfileImage(uid : String){
        viewModelScope.launch {
            profileRepository.getProfileImage(uid).collect {
                profileImage.postValue(Uri.parse(it))
            }
        }
    }

    //유저 프로필 이미지 저장
    fun uploadProfileImage(uid : String , profileUri : Uri){
        viewModelScope.launch {
            profileRepository.uploadProfileImage(uid,profileUri).collect {
                profileUploadCheck.postValue(it)
            }
        }
    }

    //유저 데이터셋 저장
    fun uploadUserDataSet(uid : String , userDTO : UserDTO){
        viewModelScope.launch {
            profileRepository.uploadUserDataSet(uid , userDTO).collect {
                userDataSetUploadCheck.postValue(it)
            }
        }
    }

    //프라그먼트에서 사용할 때 메모리 릭 방지
    fun clearData(){
        profileImage.postValue(null)
        nickName.postValue(null)
    }
}