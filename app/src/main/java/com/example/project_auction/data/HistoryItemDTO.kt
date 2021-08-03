package com.example.project_auction.data

data class HistoryItemDTO (
    var type : Int ?= null,
    var auctions : ProductAuctionDTO ? = null,
    var auctionsId : String ? = null,
    var trades : ProductTradeDTO ? = null,
    var tradesId : String ? = null

)
class HistoryType{
    companion object{
        const val auction = 0
        const val trade = 1
    }

}