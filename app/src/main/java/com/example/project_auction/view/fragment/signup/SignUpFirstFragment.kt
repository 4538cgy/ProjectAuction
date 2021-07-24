package com.example.project_auction.view.fragment.signup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.databinding.FragmentSignUpFirstBinding
import com.google.android.gms.auth.api.Auth


class SignUpFirstFragment : BaseFragment<FragmentSignUpFirstBinding>(R.layout.fragment_sign_up_first) {

    private val profileCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it != null) {
            if (it.data != null){
                photoUri = it.data!!.data.toString()
                Glide.with(binding.root.context)
                    .load(it.data!!.data)
                    .circleCrop()
                    .into(binding.fragmentSignUpFirstImageviewProfile)

                loginSignViewModel.profilePhotoUri.postValue(it.data!!.data)
            }
        }
    }
    lateinit var photoUri : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentsignupfirst = this

        binding.apply {
            fragmentSignUpFirstImageviewProfile.setOnClickListener {
                openGallery()
            }
        }
    }

    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        profileCallback.launch(intent)
    }



}