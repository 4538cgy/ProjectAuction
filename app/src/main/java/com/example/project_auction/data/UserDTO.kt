package com.example.project_auction.data

data class UserDTO(
    var uid: String? = null,
    var totalAddress: String? = null,
    var building: String? = null,
    var zipAddress: String? = null,
    var detailAddress: String? = null,
    var nickName: String? = null,
    var timestamp: Long? = null,
    //AuctionProduct Key , true/false
    var joinAuction : MutableMap<String,Boolean> ? = hashMapOf(),
    var joinAuctionCount : Long ? = null

)