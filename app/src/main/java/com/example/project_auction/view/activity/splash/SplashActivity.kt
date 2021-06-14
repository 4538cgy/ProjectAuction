package com.example.project_auction.view.activity.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            activitysplash = this@SplashActivity
        }
    }
}