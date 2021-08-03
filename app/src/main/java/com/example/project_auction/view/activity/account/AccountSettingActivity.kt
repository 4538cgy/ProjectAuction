package com.example.project_auction.view.activity.account

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivityAccountSettingBinding
import com.example.project_auction.util.auth.SignOut

class AccountSettingActivity : BaseActivity<ActivityAccountSettingBinding>(R.layout.activity_account_setting) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            activityaccountsetting = this@AccountSettingActivity

            //닫기
            activityAccountSettingImagebuttonClose.setOnClickListener {
                finish()
            }
            //로그아웃
            activityAccountSettingTextviewLogout.setOnClickListener {
                var builder = AlertDialog.Builder(binding.root.context)
                builder.apply {
                    setMessage("로그아웃 하시겠습니까?")
                    setPositiveButton("예" , DialogInterface.OnClickListener { dialog, which ->
                        SignOut(root.context,this@AccountSettingActivity).signOut()
                    })
                    setNegativeButton("아니요" , DialogInterface.OnClickListener { dialog, which ->

                    })
                    setTitle("안내")
                    show()
                }
            }

            //회원탈퇴
            activityAccountSettingTextviewDeleteAccount.setOnClickListener {
                //todo
            }
        }

        //주소 정보 가져오기
        profileViewModel.getUserAddress(auth.currentUser!!.uid)
        profileViewModel.userTotalAddress.observe(this, Observer {
            binding.activityAccountSettingTextviewAddress.text = it.toString()
        })

        //닉네임 가져오기
        profileViewModel.getUserNickName(auth.currentUser!!.uid)
        profileViewModel.nickName.observe(this, Observer {
            binding.activityAccountSettingTextviewNickname.text = it.toString()
        })

    }
}