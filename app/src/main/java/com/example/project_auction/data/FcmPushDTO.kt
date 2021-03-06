package com.example.project_auction.data

data class FcmPushDTO(
    var to : String ? = null,
    var notification : Notification ?= Notification()
){
    data class Notification(
        var body : String ? = null,
        var title : String ? = null
    )
}