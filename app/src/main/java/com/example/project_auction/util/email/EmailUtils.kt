package com.example.project_auction.util.email

import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.project_auction.BuildConfig

class EmailUtils(private val context : Context) {

    fun sendReport(){
        var intent = Intent(Intent.ACTION_SEND)
        intent.apply {
            putExtra(Intent.EXTRA_SUBJECT,"경매 앱 문의하기")
            putExtra(Intent.EXTRA_EMAIL,"4538cgy@gmail.com")
            putExtra(Intent.EXTRA_TEXT,"앱 버전 : ${BuildConfig.VERSION_NAME} \n Android SDK : ${
                Build.VERSION.SDK_INT} ${Build.VERSION.RELEASE} \n내용을 입력해주세요\n : " )
            setType("message/rfc822")
        }
        context.startActivity(intent)
    }
}