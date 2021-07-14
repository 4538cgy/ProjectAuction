package com.example.project_auction.data

data class ViewTypeModelDTO (val type: Int, val text: String, val data: Int, val contentString: String?)  {
    companion object {
        const val FIRST_TYPE = 0
        const val SECOND_TYPE = 1
        const val THERD_TYPE = 2
    }
}