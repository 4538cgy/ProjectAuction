package com.example.project_auction.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.project_auction.viewmodel.AuctionViewModel
import com.example.project_auction.viewmodel.LoginSignUpViewModel
import com.example.project_auction.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

abstract class BaseFragment <B : ViewDataBinding> (val layoutId : Int) : Fragment(){

    lateinit var binding : B
    lateinit var rootContext : Context
    val loginSignViewModel : LoginSignUpViewModel by activityViewModels()
    val auctionViewModel : AuctionViewModel by activityViewModels()
    val profileViewModel : ProfileViewModel by activityViewModels()
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater , layoutId , container , false)
        binding.lifecycleOwner = this
        rootContext = binding.root.context

        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        profileViewModel.clearData()
    }
}