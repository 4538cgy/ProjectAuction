package com.example.project_auction.view.fragment.lobby.auction

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_auction.R
import com.example.project_auction.adapter.AuctionAdapter
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.data.KakaoDTO
import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.data.TimeRequestDTO
import com.example.project_auction.databinding.FragmentAuctionBinding
import com.example.project_auction.util.http.HttpApi
import com.example.project_auction.view.activity.addpost.AddAuctionPostActivity
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuctionFragment : BaseFragment<FragmentAuctionBinding>(R.layout.fragment_auction) {

    private var isOpenFAB = false

    private var auctionData = arrayListOf<ProductAuctionDTO>()

    fun initRecyclerData(){
        val databaseReference = db.collection("productAuction")

        databaseReference.get().addOnSuccessListener {
            it?.let {
                if (!it.isEmpty) {
                    auctionData.clear()
                    var data = it.toObjects(ProductAuctionDTO::class.java)
                    auctionData.addAll(data)
                    binding.fragmentAuctionRecyclerview.adapter!!.notifyDataSetChanged()
                }
            }?.run {

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentauction = this

        initRecyclerData()



        binding.apply {
            fragmentAuctionFabMain.setOnClickListener {
                clickFab()
            }

            fragmentAuctionFabWriteAuction.setOnClickListener {
                //경매 물품 등록
                startActivity(Intent(binding.root.context,AddAuctionPostActivity::class.java))
            }

            fragmentAuctionFabWriteTrade.setOnClickListener {
                //거래 물품 등록
                var data = TimeRequestDTO.Time()
                data.day = 3

                HttpApi().test3(data).enqueue(object : Callback<TimeRequestDTO.ResponseTime> {
                    override fun onResponse(
                        call: Call<TimeRequestDTO.ResponseTime>,
                        response: Response<TimeRequestDTO.ResponseTime>
                    ) {
                        println("${response.body()}")
                    }

                    override fun onFailure(call: Call<TimeRequestDTO.ResponseTime>, t: Throwable) {
                        println("실패!")
                    }


                })
            }

            fragmentAuctionRecyclerview.adapter = AuctionAdapter(binding.root.context,auctionData)
            fragmentAuctionRecyclerview.layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)

            fragmentAuctionSwipeRefreshLayout.setOnRefreshListener {
                initRecyclerData()
                fragmentAuctionSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    fun clickFab() {
        isOpenFAB = if (!isOpenFAB) {
            ObjectAnimator.ofFloat(binding.fragmentAuctionFabWriteTrade, "translationY", -400f)
                .apply { start() }
            ObjectAnimator.ofFloat(binding.fragmentAuctionTextviewWriteTrade, "translationY", -400f)
                .apply { start() }
            ObjectAnimator.ofFloat(binding.fragmentAuctionFabWriteAuction, "translationY", -200f)
                .apply { start() }
            ObjectAnimator.ofFloat(binding.fragmentAuctionTextviewWriteAuction, "translationY", -200f)
                .apply { start() }
            binding.fragmentAuctionTextviewWriteTrade.visibility = View.VISIBLE
            binding.fragmentAuctionTextviewWriteAuction.visibility = View.VISIBLE
            true
        } else {
            ObjectAnimator.ofFloat(binding.fragmentAuctionFabWriteTrade, "translationY", -0f)
                .apply { start() }
            ObjectAnimator.ofFloat(binding.fragmentAuctionTextviewWriteTrade, "translationY", -0f)
                .apply { start() }
            ObjectAnimator.ofFloat(binding.fragmentAuctionFabWriteAuction, "translationY", -0f)
                .apply { start() }

            ObjectAnimator.ofFloat(binding.fragmentAuctionTextviewWriteAuction, "translationY", -0f)
                .apply { start() }
            binding.fragmentAuctionTextviewWriteTrade.visibility = View.INVISIBLE
            binding.fragmentAuctionTextviewWriteAuction.visibility = View.INVISIBLE
            false
        }
    }


}