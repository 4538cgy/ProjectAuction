package com.example.project_auction.view.activity.splash

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivitySplashBinding
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.uos.smsmsm.util.shareddate.PreferenceUtil

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            activitysplash = this@SplashActivity
        }

        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.fetch(0).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseRemoteConfig.fetchAndActivate()
                dialogDisplay(firebaseRemoteConfig)
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("앱 실행에 오류가 발생하였습니다. 다시 실행해주시기 바랍니다.")
                    .setCancelable(false)
                    .setPositiveButton(
                        "종료"
                    ) { _, _ -> // Apply SAM
                        this.finishAffinity()
                    }.show()
            }
        }.addOnFailureListener {
            Log.e("TEST","${it.toString()}")
        }
    }

    //앱버전 체크
    private fun appVersionCheckWithRemoteConfig() = this.packageManager.getPackageInfo(
        this.packageName,
        PackageManager.GET_ACTIVITIES
    ).versionName

    // Make private and apply Camelcase
    private fun dialogDisplay(firebaseRemoteConfig: FirebaseRemoteConfig) {
        val strVersionName = appVersionCheckWithRemoteConfig()
        // version from Firebase
        val strLatestVersion = firebaseRemoteConfig.getString("message_version")
        val strMaintenanceCheck = firebaseRemoteConfig.getBoolean("check_maintenance")

        if (strMaintenanceCheck) {
            dialogDisplayDownServer()
        } else {
            if (strVersionName != strLatestVersion) {
                AlertDialog.Builder(this)
                    .setTitle("Update")
                    .setMessage("최신 버전의 앱을 설치 후 재실행 해주시기 바랍니다.")
                    .setCancelable(false)
                    .setPositiveButton(
                        "종료"
                    ) { _, _ -> // Apply SAM
                        this.finish()
                    }.show()
            } else {
                if(PreferenceUtil(binding.root.context).getString("adultCheck","") == "true") {
                    //startActivity(Intent(this, FirstTimeActivity::class.java))
                }else{
                    //startActivity(Intent(this,WelcomeMainActivity::class.java))
                }
                this.finish()
            }
        }
    }

    private fun dialogDisplayDownServer() {
        AlertDialog.Builder(this)
            .setTitle("Maintenance")
            .setMessage("서버 점검중입니다. \n PM 06:00 ~ PM 08:00")
            .setCancelable(false)
            .setPositiveButton(
                "종료"
            ) { _, _ -> // Apply SAM
                this.finishAffinity()
            }.show()
    }
}