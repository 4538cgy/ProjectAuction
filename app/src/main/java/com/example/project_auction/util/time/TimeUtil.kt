package com.example.project_auction.util.time

import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {

    fun getTime(): String {

        return SimpleDateFormat("yyyy년 MM월 dd일 a hh시 mm분 ss초").format(Date(System.currentTimeMillis())).toString()

    }

    /** 몇분전, 방금 전,  */
    private object TIME_MAXIMUM {
        const val SEC = 60
        const val MIN = 60
        const val HOUR = 24
        const val DAY = 30
        const val MONTH = 12

        const val CLOSE_SEC = -60
        const val CLOSE_MIN = -60
        const val CLOSE_HOUR = -24
        const val CLOSE_DAY = -30
        const val CLOSE_MONTH = -12
    }

    fun getDayCheck(regTime: Long) : Boolean ? {
        val curTime = System.currentTimeMillis()
        var diffTime = (curTime - regTime) / 1000
        var check: Boolean? = null
        println("아아아아아아아아아아아아아아아아아아아아아아")
        if (diffTime < TIME_MAXIMUM.SEC) {
            println("방금 전")
            check = false
        } else if (TIME_MAXIMUM.SEC.let { diffTime /= it; diffTime } < TIME_MAXIMUM.MIN) {
            println("분 전")
            check = false
        } else if (TIME_MAXIMUM.MIN.let { diffTime /= it; diffTime } < TIME_MAXIMUM.HOUR) {
            println("시간 전")
            check = false
        } else if (TIME_MAXIMUM.HOUR.let { diffTime /= it; diffTime } < TIME_MAXIMUM.DAY) {
            println("일 전")
            check = true
        } else if (TIME_MAXIMUM.DAY.let { diffTime /= it; diffTime } < TIME_MAXIMUM.MONTH) {
            println("달 전")
            check = true
        } else {
            println("연 전")
            check = true
        }

        return check
    }

    fun formatTimeString(regTime: Long): String? {
        val curTime = System.currentTimeMillis()
        var diffTime = (curTime - regTime) / 1000
        var msg: String? = null
        if (diffTime < TIME_MAXIMUM.SEC) {
            msg = "방금 전"
        } else if (TIME_MAXIMUM.SEC.let { diffTime /= it; diffTime } < TIME_MAXIMUM.MIN) {
            msg = diffTime.toString() + "분 전"
        } else if (TIME_MAXIMUM.MIN.let { diffTime /= it; diffTime } < TIME_MAXIMUM.HOUR) {
            msg = diffTime.toString() + "시간 전"
        } else if (TIME_MAXIMUM.HOUR.let { diffTime /= it; diffTime } < TIME_MAXIMUM.DAY) {
            msg = diffTime.toString() + "일 전"
        } else if (TIME_MAXIMUM.DAY.let { diffTime /= it; diffTime } < TIME_MAXIMUM.MONTH) {
            msg = diffTime.toString() + "달 전"
        } else {
            msg = diffTime.toString() + "년 전"
        }
        return msg
    }

    fun formatCloseTimeString(regTime: Long): String? {

        var msg = ""

        var HHmm = SimpleDateFormat("HH:mm",Locale.KOREAN)
        var ddHHmm = SimpleDateFormat("dd일 HH:mm", Locale.KOREAN)
        val timezone = TimeZone.getTimeZone("Asia/seoul")
        msg = if ((regTime-System.currentTimeMillis()) < 86400000){
            HHmm.timeZone = timezone
            return HHmm.format(regTime-System.currentTimeMillis())
        }else{
            ddHHmm.timeZone = timezone
            return ddHHmm.format(regTime-System.currentTimeMillis())
        }


        return msg
    }
}