package com.example.project_auction.view.fragment.signup

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.project_auction.R
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.databinding.FragmentSignUpSecondBinding
import com.google.firebase.firestore.FirebaseFirestore

class SignUpSecondFragment : BaseFragment<FragmentSignUpSecondBinding>(R.layout.fragment_sign_up_second) {

    private var nickCheck : Boolean = false
    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentsignupsecond = this

        binding.apply {
            fragmentSignUpSecondEdittextNick.addTextChangedListener {
                updateView(it!!.length)
                loginSignViewModel.profileNickName.postValue(it.toString())
            }
            fragmentSignUpSecondButtonNickCheck.setOnClickListener {
                db.collection("nickname").
            }
        }
    }

    fun updateView(count : Int){
        binding.fragmentSignUpSecondTextviewTextCount.text = "$count/10"

        if (count > 1){
            binding.fragmentSignUpSecondButtonNickCheck.apply {
                isEnabled = true
                setBackgroundResource(R.drawable.background_round_black_4dp)
                setTextColor(Color.WHITE)
            }
        }else{
            binding.fragmentSignUpSecondButtonNickCheck.apply {
                isEnabled = false
                setBackgroundResource(R.drawable.background_round_gray_stroke_black_4dp)
                setTextColor(Color.BLACK)
            }
        }
    }

}