package com.example.project_auction.data

import com.google.gson.annotations.SerializedName

data class NaverDTO(
    var access_token : String ? = null


){
    data class NaverResponse(
        @SerializedName("code")
        var code : String ?,
        @SerializedName("data")
        var data : String ?,
        @SerializedName("msg")
        var msg : String ?
    )
}