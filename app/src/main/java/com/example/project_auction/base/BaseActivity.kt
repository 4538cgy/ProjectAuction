package com.example.project_auction.base

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.project_auction.viewmodel.AddProductViewModel
import com.example.project_auction.viewmodel.AuctionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

abstract class BaseActivity<B : ViewDataBinding>(val layoutId: Int) : AppCompatActivity() {

    lateinit var binding: B
    lateinit var rootContext: Context
    val addProductViewModel: AddProductViewModel by viewModels()
    val auctionViewModel : AuctionViewModel by viewModels()
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        rootContext = binding.root.context
    }


}