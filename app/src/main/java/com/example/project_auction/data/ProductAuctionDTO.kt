package com.example.project_auction.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProductAuctionDTO (
    var uid : String ? = null,
    var photoList : ArrayList<String>? =null,
    var category : String ? = null,
    var title : String ? = null,
    var timestamp : Long ? = null,
    var closeTimestamp : Long ? = null,
    var startCost : String ? = null,
    var currentCost : String ? = null,
    var closeCost : String ? = null,
    var content : String ? = null,
    var delete : Boolean = false,
    var viewCount : Long = 0,
    var joinCount : Long = 0,
    //Map<uid,true or false>
    var joiners : MutableMap<String,Boolean> = HashMap(),
    // Map<uid,true or false>
    var viewers : MutableMap<String,Boolean> = HashMap(),
    var favoriteCount : Long = 0,
    // Map<uid,true or false>
    var favoriters : MutableMap<String,Boolean> = HashMap()

) : Serializable {
    data class ProductResponseDTO(
        @SerializedName("data")
        var data : ArrayList<ProductAuctionDTO> = arrayListOf()
    )
}
