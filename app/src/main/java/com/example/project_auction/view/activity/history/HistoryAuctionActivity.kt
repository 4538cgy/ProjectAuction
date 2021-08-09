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
import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.databinding.ActivityHistoryAuctionBinding

class HistoryAuctionActivity : BaseActivity<ActivityHistoryAuctionBinding>(R.layout.activity_history_auction) {

    private var list = ArrayList<HistoryItemDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            activityhistoryauction = this@HistoryAuctionActivity
            //종료
            activityHistoryAuctionImagebuttonClose.setOnClickListener {
                finish()
            }
        }

        //데이터 가져오기
        getData(intent.getStringExtra("HistoryType").toString())

        //리사이클러뷰 부착
        initRecyclerAdapter()

        //내가 참여한 경매물품 데이터 관찰
        auctionViewModel.myBiddedAUctionData.observe(this, Observer {
            it!!.forEach {
                list.add(HistoryItemDTO(HistoryType.auction,it.value,it.key))
            }
            binding.activityHistoryAuctionRecycler.adapter!!.notifyDataSetChanged()
        })

        //내 경매물품 데이터 관찰
        auctionViewModel.myAuctionData.observe(this , Observer {
            it!!.forEach {
                list.add(HistoryItemDTO(HistoryType.auction,it.value,it.key))
            }
            binding.activityHistoryAuctionRecycler.adapter!!.notifyDataSetChanged()
        })

    }

    fun getData(historyType : String){
        when(historyType){
            "BIDDED" -> {
                auctionViewModel.getBiddedAuctionData(auth.currentUser!!.uid)
            }
            "MYAUCTION" ->{
                auctionViewModel.getMyAuctionData(auth.currentUser!!.uid)
            }
        }
    }

    fun initRecyclerAdapter(){
        binding.activityHistoryAuctionRecycler.adapter = HistoryAdapter(list)
        binding.activityHistoryAuctionRecycler.layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)
    }
}