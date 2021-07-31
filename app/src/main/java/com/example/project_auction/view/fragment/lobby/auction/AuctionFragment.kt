package com.example.project_auction.view.fragment.lobby.auction

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.algolia.search.saas.Client
import com.example.project_auction.R
import com.example.project_auction.adapter.AuctionAdapter
import com.example.project_auction.adapter.TradeAdapter
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.data.*
import com.example.project_auction.databinding.FragmentAuctionBinding
import com.example.project_auction.util.http.HttpApi
import com.example.project_auction.util.itemdcorator.GridItemDecorator
import com.example.project_auction.view.activity.addpost.AddAuctionPostActivity
import com.example.project_auction.view.activity.addpost.AddTradePostActivity
import com.example.project_auction.view.bottomsheet.BottomSheetAuctionMenu
import com.google.firebase.firestore.Query
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuctionFragment : BaseFragment<FragmentAuctionBinding>(R.layout.fragment_auction) {

    private var isOpenFAB = false
    private var viewState = "Auction" // default = Auction , [ Auction, Trade ]
    private var auctionData = arrayListOf<ProductAuctionDTO>()
    private var auctionDataId = arrayListOf<String>()
    private var tradeData = arrayListOf<ProductTradeDTO>()
    private var tradeDataId = arrayListOf<String>()
    private val client = Client("PRG26POP2U","8a97c353cba4db57d0853c46f9d5b0f1")
    private val index = client.getIndex("helloworld")
    private var gridItemDecoratorCheck = false

    private var page = 1




    fun initRecyclerData(){
        if(viewState == "Auction") {
            binding.fragmentAuctionRecyclerview.adapter = AuctionAdapter(binding.root.context, auctionData, auctionDataId)
            binding.fragmentAuctionRecyclerview.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            (binding.fragmentAuctionRecyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }else if (viewState == "Trade"){
            binding.fragmentAuctionRecyclerview.adapter = TradeAdapter(binding.root.context,tradeData,tradeDataId)
            binding.fragmentAuctionRecyclerview.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            (binding.fragmentAuctionRecyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        /*
        if (viewState == "Auction") {
            println("옥션 부착")
            val databaseReference =
                db.collection("productAuction").orderBy("timestamp", Query.Direction.DESCENDING)

            databaseReference.get().addOnSuccessListener {
                it?.let {
                    if (!it.isEmpty) {
                        auctionData.clear()
                        auctionDataId.clear()
                        var data = it.toObjects(ProductAuctionDTO::class.java)
                        auctionData.addAll(data)
                        it.forEach { queryDocumentSnapshot ->
                            auctionDataId.add(queryDocumentSnapshot.id)
                        }

                        println("옥션 부착2")
                        binding.fragmentAuctionRecyclerview.adapter = null
                        binding.fragmentAuctionRecyclerview.adapter = AuctionAdapter(binding.root.context,auctionData,auctionDataId)
                        binding.fragmentAuctionRecyclerview.layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)
                        binding.fragmentAuctionRecyclerview.adapter!!.notifyDataSetChanged()
                    }
                }?.run {

                }
            }

            binding.fragmentAuctionButtonTrade.setBackgroundResource(R.drawable.background_round_gray_24dp)
            binding.fragmentAuctionButtonAuction.setBackgroundResource(R.drawable.background_round_yellow_24dp)

        }else if (viewState == "Trade"){
            println("거래 부착")
            val databaseReference = db.collection("ProductTrade").orderBy("timestamp",Query.Direction.DESCENDING)

            databaseReference.get().addOnSuccessListener {
                it.let {
                    if (!it.isEmpty){
                        tradeData.clear()
                        tradeDataId.clear()
                        var data = it.toObjects(ProductTradeDTO::class.java)
                        tradeData.addAll(data)
                        it.forEach { queryDocumentSnapshot ->
                            tradeDataId.add(queryDocumentSnapshot.id)
                        }

                        println("거래 부착2")
                        binding.fragmentAuctionRecyclerview.adapter = null
                        binding.fragmentAuctionRecyclerview.adapter = TradeAdapter(binding.root.context,tradeData,tradeDataId)
                        if (!gridItemDecoratorCheck) {
                            binding.fragmentAuctionRecyclerview.addItemDecoration(
                                GridItemDecorator(
                                    20
                                )
                            )
                            gridItemDecoratorCheck = true
                        }
                        binding.fragmentAuctionRecyclerview.layoutManager = GridLayoutManager(binding.root.context,3)
                        binding.fragmentAuctionRecyclerview.adapter!!.notifyDataSetChanged()
                    }
                }?.run{

                }
                binding.fragmentAuctionButtonTrade.setBackgroundResource(R.drawable.background_round_yellow_24dp)
                binding.fragmentAuctionButtonAuction.setBackgroundResource(R.drawable.background_round_gray_24dp)
            }
        }

         */
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentauction = this
        binding.lifecycleOwner = this
        binding.auctionviewmodel = auctionViewModel

        auctionData.clear()
        auctionDataId.clear()

        initRecyclerData()

        binding.fragmentAuctionRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.fragmentAuctionRecyclerview.canScrollVertically(1)) {
                    println("끝에 도달")

                    binding.fragmentAuctionButtonLoadMore.visibility = View.VISIBLE

                }else{
                    binding.fragmentAuctionButtonLoadMore.visibility = View.GONE
                }
            }
        })

        auctionViewModel.auctionData.value = null
        auctionViewModel.tradeData.value = null

        auctionViewModel.tradeData.observe(viewLifecycleOwner, object  : Observer<ProductTradeDTO.ProductResponseDTO?>{
            override fun onChanged(t: ProductTradeDTO.ProductResponseDTO?) {
                if (t != null){

                }else{
                    println("데이터의 마지막입니다.")
                }
            }
        })

        auctionViewModel.loadAuctionData(1,-1,auth.currentUser!!.uid,"timestamp")
        auctionViewModel.auctionData.observe(viewLifecycleOwner, object: Observer<ProductAuctionDTO.ProductResponseDTO?>{
            override fun onChanged(t: ProductAuctionDTO.ProductResponseDTO?) {
                if (t != null) {
                    auctionData.clear()
                    auctionDataId.clear()
                    t!!.data.forEach {
                        auctionData.add(it)
                    }

                    println("데이터 삽입전 ${t.dataId.toString()}")


                    t!!.dataId.forEach {
                        auctionDataId.add(it)
                    }
                    page ++
                    println("데이터 삽입후 ${auctionDataId.toString()}")
                    binding.fragmentAuctionRecyclerview.adapter!!.notifyDataSetChanged()
                }else{
                        println("데이터의 마지막 입니다.")
                }

            }
        })




        var query = com.algolia.search.saas.Query("테스트")
            query.apply {
                setAttributesToRetrieve("title","content","timestamp")
            }
        /*
        index.searchAsync(query
        ) { p0, p1 ->

            println("p0 = ${p0.toString()}")
            println("아아2 = ${p0!!.getJSONArray("hits")}")

            var json = p0.getJSONArray("hits")

            for (i in 0 .. json.length()-1){

                var jsonData = json.getJSONObject(i)

                println("$i 번째의 title = ${jsonData.getString("title")}")
                println("$i 번째의 content = ${jsonData.getString("content")}")
            }

            println("p1 = ${p1.toString()}")
        }

         */



        binding.apply {

            //플로팅 버튼
            fragmentAuctionFabMain.setOnClickListener {
                clickFab()
            }

            //게시글 더 가져오기 버튼
            fragmentAuctionButtonLoadMore.setOnClickListener {
                if (viewState == "Auction") {
                    auctionViewModel.loadAuctionData(page, -1, auth.currentUser!!.uid, "timestamp")
                }else if(viewState == "Trade"){
                    auctionviewmodel.loadTradeData(page, -1, auth.currentUser!!.uid,"timestamp",true)
                }
            }

            fragmentAuctionFabWriteAuction.setOnClickListener {
                //경매 물품 등록
                startActivity(Intent(binding.root.context,AddAuctionPostActivity::class.java))
            }

            fragmentAuctionFabWriteTrade.setOnClickListener {
                //거래 물품 등록

                startActivity(Intent(binding.root.context,AddTradePostActivity::class.java))
            }

            fragmentAuctionBackground.setOnClickListener {
                clickFab()
                fragmentAuctionBackground.setBackgroundResource(R.color.colorTransparent)
                fragmentAuctionBackground.visibility = View.GONE
            }

            //옥션 버튼
            fragmentAuctionButtonAuction.setOnClickListener {
                println("옥션으로 변경")
                viewState = "Auction"
                initRecyclerData()
            }

            //거래 버튼
            fragmentAuctionButtonTrade.setOnClickListener {
                println("거래로 변경")
                viewState = "Trade"
                initRecyclerData()
            }
            
            //더보기 버튼
            fragmentAuctionImagebuttonMore.setOnClickListener { 
                val auctionMenu = BottomSheetAuctionMenu()
                auctionMenu.show(requireActivity().supportFragmentManager,"lol")
            }

            fragmentAuctionSwipeRefreshLayout.setOnRefreshListener {
                initRecyclerData()
                fragmentAuctionSwipeRefreshLayout.isRefreshing = false
            }
        }

        auctionViewModel.auctionCategory.observe(viewLifecycleOwner, Observer {
            updateRecyclerDataByCategory(it)
        })
    }


    fun updateRecyclerDataByCategory(category : String){
        println(category)
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
            binding.fragmentAuctionBackground.setBackgroundResource(R.color.colorBlackTransparent)
            binding.fragmentAuctionBackground.visibility = View.VISIBLE
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
            binding.fragmentAuctionBackground.setBackgroundResource(R.color.colorTransparent)
            false
        }
    }




}