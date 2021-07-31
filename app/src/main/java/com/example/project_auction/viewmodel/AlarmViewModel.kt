package com.example.project_auction.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_auction.data.AlarmDTO
import com.example.project_auction.repository.AlarmRepository.Companion.alarmRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AlarmViewModel : ViewModel() {

    var alarmDatas = MutableLiveData<ArrayList<AlarmDTO>>()

    fun loadAlarmDatas(uid : String){
        viewModelScope.launch {
            alarmRepository.loadAlarmData(uid).collect {
                alarmDatas.postValue(it)
            }
        }
    }
}