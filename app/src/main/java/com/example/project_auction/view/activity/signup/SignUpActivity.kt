package com.example.project_auction.view.activity.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.project_auction.R
import com.example.project_auction.adapter.SignUpViewPagerAdapter
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivitySignUpBinding
import com.example.project_auction.view.fragment.signup.SignUpFirstFragment
import com.example.project_auction.view.fragment.signup.SignUpSecondFragment
import com.example.project_auction.view.fragment.signup.SignUpThirdFragment
import com.example.project_auction.viewmodel.LoginSignUpViewModel

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {

    private val viewModel: LoginSignUpViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            activitysignup = this@SignUpActivity
            loginsignupviewmodel = viewModel

            val fragmentList = arrayListOf<Fragment>(
                SignUpFirstFragment(),
                SignUpSecondFragment(),
                SignUpThirdFragment()
            )
            val adapter = SignUpViewPagerAdapter(this@SignUpActivity)
            adapter.fragments = fragmentList
            activitySignUpViewpager2.apply {
                this.adapter = adapter
                this.run { isUserInputEnabled = false }
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        updateView(position)
                    }
                })
            }

            activitySignUpButtonNext.setOnClickListener {
                activitySignUpViewpager2.currentItem = activitySignUpViewpager2.currentItem + 1
            }


        }
        viewModel.profilePhotoUri.observe(this, Observer {
            binding.activitySignUpButtonNext.isEnabled = true
        })
        viewModel.nickNameNextButtonState.observe(this, Observer {
            binding.activitySignUpButtonNext.isEnabled = it
        })
    }

    fun updateView(position: Int) {
        when (position) {
            0 -> {
                binding.activitySignUpButtonNext.isEnabled = false
                binding.activitySignUpTextviewTitle.text = "프로필 설정"
                binding.activitySignUpTextviewPosition.text = "회원 가입 1 / 3"
            }
            1 -> {
                binding.activitySignUpButtonNext.isEnabled = false
                binding.activitySignUpTextviewTitle.text = "닉네임 설정"
                binding.activitySignUpTextviewPosition.text = "회원 가입 2 / 3"
            }
            2 -> {
                binding.activitySignUpTextviewTitle.text = "주소지 설정"
                binding.activitySignUpTextviewPosition.text = "회원 가입 3 / 3"
                binding.activitySignUpButtonNext.text = "완료"
            }
        }
    }

}