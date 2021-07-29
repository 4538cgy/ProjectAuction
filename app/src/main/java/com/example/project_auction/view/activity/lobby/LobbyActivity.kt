package com.example.project_auction.view.activity.lobby

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivityLobbyBinding
import com.example.project_auction.view.fragment.lobby.account.AccountFragment
import com.example.project_auction.view.fragment.lobby.alarm.AlarmFragment
import com.example.project_auction.view.fragment.lobby.auction.AuctionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.installations.FirebaseInstallations

class LobbyActivity : BaseActivity<ActivityLobbyBinding>(R.layout.activity_lobby), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerPushToken()

        getProfileImage()


        binding.apply {
            activitylobby = this@LobbyActivity
            activityLobbyBottomNavigation.setOnNavigationItemSelectedListener(this@LobbyActivity)
            activityLobbyBottomNavigation.selectedItemId = R.id.action_auction

        }
    }

    override fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_auction -> {
            val auctionFragment = AuctionFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_lobby_fragmelayout, auctionFragment).commit()
            true
        }
        R.id.action_alarm -> {
            val alarmFragment = AlarmFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_lobby_fragmelayout, alarmFragment).commit()
            true
        }
        R.id.action_account -> {
            val accountFragment = AccountFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_lobby_fragmelayout, accountFragment).commit()
            true
        }
        else -> false
    }

    private fun getProfileImage(){
        db.collection("UserProfileImages").document(auth.currentUser!!.uid).get().addOnCompleteListener {
            it?.let {
                //none null todo
                if (it.isSuccessful){
                    val url = it.result!!["image"].toString()
                    println("uid ${auth.currentUser!!.uid}")
                    println("사진 데이터 ${url.toString()}")

                    var requestBuilder = Glide.with(binding.root.context).asBitmap()

                    requestBuilder.load(url).circleCrop().into(object : CustomTarget<Bitmap?>(){
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            println("")
                            var menu = binding.activityLobbyBottomNavigation.menu
                            binding.activityLobbyBottomNavigation.itemIconTintList = null
                            menu.findItem(R.id.action_account).setIcon(BitmapDrawable(applicationContext.resources,resource))
                            //menu.findItem(R.id.action_account).setIcon(R.drawable.naver_icon)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            TODO("Not yet implemented")
                        }
                    })



                }
            }?.run {
                //null todo
                println("null이야")
            }
        }.addOnFailureListener {
            println("실패 ${it.toString()}")
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed()

        AlertDialog.Builder(rootContext).apply {
            setMessage("종료하시겠습니까?")
            setPositiveButton("아니오", null)
            setNegativeButton(
                "예"
            ) { _, _ -> finishAffinity() } // Apply SAM
            setTitle("안내")
        }.show()
    }

    private fun registerPushToken(){
        println("레지스터 등록")
        /*
        val pushToken = FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener {
            val uid = auth.currentUser!!.uid
            val map = mutableMapOf<String,Any>()
            map.put("pushToken",it.result.token.toString())

            db.collection("pushTokens").document(uid).set(map)
        }

         */
        val map = mutableMapOf<String,Any>()
        map.put("pushToken", FirebaseInstanceId.getInstance().token.toString())

        db.collection("pushTokens").document(auth.currentUser!!.uid).set(map)

    }
}