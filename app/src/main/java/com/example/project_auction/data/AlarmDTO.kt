package com.example.project_auction.data

data class AlarmDTO(
    var uid : String ? =null,
    var targetUid : String ? = null,
    var message : String ? = null,
    var timestamp : Long ? = null,
    //kind 11 = 경매 참여알람 , 12 = 경매 종료 알람 , 13 = 즉시 입찰 알람 , 21 = 거래 채팅 알람 , 22 = 거래 완료 알람
    var alarmKind : Long ? = null
)