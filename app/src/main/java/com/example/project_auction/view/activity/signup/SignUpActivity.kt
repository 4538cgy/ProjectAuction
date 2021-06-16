package com.example.project_auction.view.activity.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.project_auction.R
import com.example.project_auction.adapter.SignUpViewPagerAdapter
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivitySignUpBinding
import com.example.project_auction.view.fragment.signup.SignUpFirstFragment
import com.example.project_auction.viewmodel.LoginSignUpViewModel

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {

    private val viewModel : LoginSignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            activitysignup = this@SignUpActivity
            loginsignupviewmodel = viewModel

            val fragmentList = arrayListOf<Fragment>(SignUpFirstFragment())
            val adapter = SignUpViewPagerAdapter(this@SignUpActivity)
            adapter.fragments = fragmentList
            activitySignUpViewpager2.apply {
                this.adapter = adapter
                this.run { isUserInputEnabled = false }
            }
        }
    }

}