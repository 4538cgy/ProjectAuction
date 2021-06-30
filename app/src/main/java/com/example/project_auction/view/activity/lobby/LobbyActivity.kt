package com.example.project_auction.view.activity.lobby

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivityLobbyBinding
import com.example.project_auction.view.fragment.lobby.account.AccountFragment
import com.example.project_auction.view.fragment.lobby.alarm.AlarmFragment
import com.example.project_auction.view.fragment.lobby.auction.AuctionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class LobbyActivity : BaseActivity<ActivityLobbyBinding>(R.layout.activity_lobby), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}