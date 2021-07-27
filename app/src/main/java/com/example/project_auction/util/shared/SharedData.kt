package com.example.project_auction.util.shared

import android.app.Application

// Apply ktlint
class SharedData : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }
    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}
