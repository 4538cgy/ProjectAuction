package com.example.project_auction.data

data class TimeRequestDTO(
    var type : String ? = null
){
    data class Time(
        var day : Int ? = null
    )

    data class ResponseTime(
        var nowTime : Long ? = null,
        var afterTime : Long ? = null
    )
}