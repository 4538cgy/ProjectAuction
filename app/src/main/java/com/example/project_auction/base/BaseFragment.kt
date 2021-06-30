package com.example.project_auction.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment <B : ViewDataBinding> (val layoutId : Int) : Fragment(){

    lateinit var binding : B
    lateinit var rootContext : Context

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
}