package com.example.project_auction.data

data class PostRequestDTO(
    var page : Int ? = null,
    var orderBy : Int ? = null,
    var uid : String ? = null,
    var sortKey : String ? = null
)