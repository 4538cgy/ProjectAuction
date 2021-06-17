package com.example.project_auction.view.activity.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivitySignUpLobbyBinding

class SignUpLobbyActivity : BaseActivity<ActivitySignUpLobbyBinding>(R.layout.activity_sign_up_lobby) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activitysignuplobby = this

        binding.activitySignUpLobbyImagebuttonClose.setOnClickListener { 
            finish()
        }
        
        binding.activitySignUpButtonSignUpGoogle.setOnClickListener { 
            println("구글로 회원가입")
        }
        
        binding.activitySignUpLobbyButtonSignUpKakao.setOnClickListener { 
            println("카카오로 회원 가입")
        }
        
        binding.activitySignUpLobbyButtonSignUpNaver.setOnClickListener { 
            println("네이버로 회원 가입")
        }
        
        binding.activitySignUpLobbyButtonSignUpEmail.setOnClickListener { 
            println("이메일로 회원가입")
        }
    }
}

