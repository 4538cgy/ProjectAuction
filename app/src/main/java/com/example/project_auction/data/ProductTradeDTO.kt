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
    var favoriters : MutableMap<String,Boolean> = HashMap(),
    //거래 방법 [ 직거래 / 택배거래 ]
    var tradeMethod : String ? = null,
    //신상품 여부 [ 신상품 / 중고상품 ]
    var productState : String ? = null,
    //교환 여부
    var exchangeState : String ? = null

) : Serializable {
    data class ProductResponseDTO(
        @SerializedName("data")
        var data : ArrayList<ProductTradeDTO> = arrayListOf()
    )
}