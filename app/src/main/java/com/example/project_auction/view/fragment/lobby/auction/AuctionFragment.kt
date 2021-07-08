package com.example.project_auction.view.fragment.lobby.auction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project_auction.R
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.databinding.FragmentAuctionBinding

class AuctionFragment : BaseFragment<FragmentAuctionBinding>(R.layout.fragment_auction) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentauction = this
    }


}