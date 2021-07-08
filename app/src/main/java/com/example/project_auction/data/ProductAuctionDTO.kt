package com.example.project_auction.data

class ProductAuctionDTO (
    var uid : String ? = null,
    var photoList : ArrayList<String>? =null,
    var category : String ? = null,
    var title : String ? = null,
    var timestamp : String ? = null,
    var closeTimestamp : String ? = null,
    var startCost : String ? = null,
    var closeCost : String ? = null,
    var content : String ? = null,
    var delete : Boolean = false,
    var viewCount : Long = 0,
    // Map<uid,nickname>
    var viewers : MutableMap<String,String> = HashMap(),
    var favoriteCount : Long = 0,
    // Map<uid,nickname>
    var favoriters : MutableMap<String,String> = HashMap()

)
