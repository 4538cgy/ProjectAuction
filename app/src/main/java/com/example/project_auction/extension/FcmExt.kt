package com.example.project_auction.extension

import android.app.Activity
import android.widget.Toast
import com.example.project_auction.util.fcm.FcmPush

//kind 11 = 경매 참여알람 , 12 = 경매 종료 알람 , 13 = 즉시 입찰 알람 , 21 = 거래 채팅 알람 , 22 = 거래 완료 알람
fun fcmPush(message : String,kind : Int, uid: String){

    val fcm = FcmPush()

    when(kind){
        11 ->{
            fcm.sendMessage(uid,"경매 참여 알람",message)
        }
    }
}
