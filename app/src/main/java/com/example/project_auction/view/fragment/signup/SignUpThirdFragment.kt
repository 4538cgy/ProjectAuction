package com.example.project_auction.view.fragment.signup

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.databinding.FragmentSignUpThirdBinding
import com.example.project_auction.util.address.SearchAddressActivity


class SignUpThirdFragment : BaseFragment<FragmentSignUpThirdBinding>(R.layout.fragment_sign_up_third) {

    private val addressCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it != null){
            println("args1 [우편번호] = ${it.data!!.getStringExtra("address_arg1")}" +
                    "args2 [지번] = ${it.data!!.getStringExtra("address_arg2")}" +
                    "args3 [건물] = ${it.data!!.getStringExtra("address_arg3")}")

            updateView(it.data!!.getStringExtra("address_arg2")!!,
                it.data!!.getStringExtra("address_arg3")!!
            )

            loginSignViewModel.buildingAddress.postValue(it.data!!.getStringExtra("address_arg3"))
            loginSignViewModel.address.postValue(it.data!!.getStringExtra("address_arg2"))
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentsignupthird = this

        binding.apply {
            fragmentSignUpButtonSearchAddress.setOnClickListener {
                val intent = Intent(binding.root.context,SearchAddressActivity::class.java)
                addressCallback.launch(intent)
            }

            fragmentSignUpThirdEdittextDetailAddress.addTextChangedListener {
                if (it!!.isNotEmpty()){
                    if (binding.fragmentSignUpThirdEdittextAddress.text.isNotEmpty()) {
                        loginSignViewModel.addressCheck.postValue(true)
                        loginSignViewModel.detailAddress.postValue(it.toString())
                    }
                }
            }
        }
    }

    fun updateView(address : String, buildingName : String){
        var totalAddress = address + buildingName
        binding.fragmentSignUpThirdEdittextAddress.setText(totalAddress)
    }
}