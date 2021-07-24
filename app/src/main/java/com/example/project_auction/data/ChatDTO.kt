package com.example.project_auction.data

data class ChatDTO(
    var lastTimestamp : Long ? = null,
    var users : MutableMap<String,Boolean> = HashMap(),
    var comments : ArrayList<ChatData> = arrayListOf(),
    var postId : String ? = null
){
    data class ChatData(
        var message : String ? =null,
        var timestamp : Long ? = null,
        var uid : String ? = null
    )
}