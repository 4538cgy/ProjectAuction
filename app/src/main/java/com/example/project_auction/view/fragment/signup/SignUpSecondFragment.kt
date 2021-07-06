package com.example.project_auction.view.fragment.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project_auction.R
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.databinding.FragmentSignUpSecondBinding

class SignUpSecondFragment : BaseFragment<FragmentSignUpSecondBinding>(R.layout.fragment_sign_up_second) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentsignupsecond = this
    }
}