package com.example.project_auction.data

import com.google.gson.annotations.SerializedName

data class TimeRequestDTO(
    var type : String ? = null
){
    data class Time(
        var day : Int ? = null
    )

    data class TimeResponse(
        var code : Int ? = null,
        var data : data ? = null,
        var msg : String ? =null
    )

    data class data(
        @SerializedName("nowTime")
        var nowTime : Long ? = null,
        @SerializedName("afterTime")
        var afterTime : Long ? = null
    )
}