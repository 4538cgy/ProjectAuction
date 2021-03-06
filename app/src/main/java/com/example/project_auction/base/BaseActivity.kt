package com.example.project_auction.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.example.project_auction.viewmodel.AlarmViewModel
import com.example.project_auction.viewmodel.AuctionViewModel
import com.example.project_auction.viewmodel.LoginSignUpViewModel
import com.example.project_auction.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

abstract class BaseActivity<B : ViewDataBinding>(val layoutId: Int) : AppCompatActivity() {

    lateinit var binding: B
    lateinit var rootContext: Context

    val auctionViewModel : AuctionViewModel by viewModels()
    val loginSignViewModel : LoginSignUpViewModel by viewModels()
    val profileViewModel : ProfileViewModel by viewModels()
    val alarmViewModel : AlarmViewModel by viewModels()
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        rootContext = binding.root.context
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
    }




}