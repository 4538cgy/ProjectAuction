package com.example.project_auction.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductTradeDTO(
    var uid : String ? = null,
    var photoList : ArrayList<String>? =null,
    var category : String ? = null,
    var title : String ? = null,
    var timestamp : Long ? = null,
    var cost : String ? = null,
    var content : String ? = null,
    var delete : Boolean = false,
    var viewCount : Long = 0,
    // Map<uid,true or false>
    var viewers : MutableMap<String,Boolean> = HashMap(),
    var favoriteCount : Long = 0,
    // Map<uid,true or false>
    var favoriters : MutableMap<String,Boolean> = HashMap()

) : Serializable {
    data class ProductResponseDTO(
        @SerializedName("data")
        var data : ArrayList<ProductTradeDTO> = arrayListOf()
    )
}