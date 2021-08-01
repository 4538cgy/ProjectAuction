package com.example.project_auction.data

import androidx.annotation.DrawableRes

data class SettingItem(
    val type: Int,
    @DrawableRes
    val icon: Int ?= null,
    val title: String ? = null,
    val line: Boolean = false
)

class SettingType{
    companion object{
        const val SUCCESS_BIDDED_LIST = 101
        const val MY_BID_LIST = 102
        const val BUY_LIST = 103
        const val SELL_LIST = 104
        const val EVENT = 105
        const val SETTING_APP = 106
        const val SETTING_ACCOUNT = 107
        const val NOTICE = 108
        const val FAQ = 109
        const val TITLE = 100
    }
}