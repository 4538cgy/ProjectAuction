package com.example.project_auction.base

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity <B : ViewDataBinding>(val layoutId: Int) : AppCompatActivity(){

    lateinit var binding : B
    lateinit var rootContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,layoutId)
        binding.lifecycleOwner = this
        rootContext = binding.root.context
    }

}