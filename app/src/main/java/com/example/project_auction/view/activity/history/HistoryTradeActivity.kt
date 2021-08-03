package com.example.project_auction.view.activity.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_auction.R
import com.example.project_auction.adapter.HistoryAdapter
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.data.HistoryItemDTO
import com.example.project_auction.data.HistoryType
import com.example.project_auction.databinding.ActivityHistoryTradeBinding

class HistoryTradeActivity : BaseActivity<ActivityHistoryTradeBinding>(R.layout.activity_history_trade) {

    private var list = ArrayList<HistoryItemDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            activityhistorytrade = this@HistoryTradeActivity


            //닫기
            activityHistoryTradeImagebuttonClose.setOnClickListener {
                finish()
            }
            initRecyclerView()

            //내 판매 목록 요청
            auctionViewModel.getMyTradeData(auth.currentUser!!.uid)
            auctionViewModel.myTradeData.observe(this@HistoryTradeActivity , Observer {
                it!!.forEach {
                    list.add(HistoryItemDTO(HistoryType.trade,null,null,it.value,it.key))
                }
                activityHistoryTradeRecycler.adapter!!.notifyDataSetChanged()
            })
        }
    }

    fun initRecyclerView(){
        binding.activityHistoryTradeRecycler.adapter = HistoryAdapter(list)
        binding.activityHistoryTradeRecycler.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL,false)
    }
}