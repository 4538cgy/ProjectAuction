package com.example.project_auction.view.fragment.lobby.auction

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.search.saas.Client
import com.example.project_auction.R
import com.example.project_auction.adapter.AuctionAdapter
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.data.*
import com.example.project_auction.databinding.FragmentAuctionBinding
import com.example.project_auction.util.http.HttpApi
import com.example.project_auction.view.activity.addpost.AddAuctionPostActivity
import com.google.firebase.firestore.Query
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuctionFragment : BaseFragment<FragmentAuctionBinding>(R.layout.fragment_auction) {

    private var isOpenFAB = false
    private var viewState = "Auction" // default = Auction , [ Auction, Trade ]
    private var auctionData = arrayListOf<ProductAuctionDTO>()
    private var auctionDataId = arrayListOf<String>()
    private val client = Client("PRG26POP2U","8a97c353cba4db57d0853c46f9d5b0f1")
    private val index = client.getIndex("helloworld")

    fun initRecyclerData(){

        if (viewState == "Auction") {
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
                        binding.fragmentAuctionRecyclerview.adapter!!.notifyDataSetChanged()
                    }
                }?.run {

                }
            }
        }else if (viewState == "Trade"){
            val databaseReference = db.collection("productTrade").orderBy("timestamp",Query.Direction.DESCENDING)

            databaseReference.get().addOnSuccessListener {
                it.let {

                }?.run{

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentauction = this

        initRecyclerData()


        var query = com.algolia.search.saas.Query("테스트")
            query.apply {
                setAttributesToRetrieve("title","content","timestamp")
            }

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



        binding.apply {

            //플로팅 버튼
            fragmentAuctionFabMain.setOnClickListener {
                clickFab()
            }

            fragmentAuctionFabWriteAuction.setOnClickListener {
                //경매 물품 등록
                startActivity(Intent(binding.root.context,AddAuctionPostActivity::class.java))
            }

            fragmentAuctionFabWriteTrade.setOnClickListener {
                //거래 물품 등록





                //게시글 조회 요청
                var data2 = PostRequestDTO()
                data2.uid = auth.currentUser!!.uid.toString()
                data2.page = 2
                data2.orderBy = 1
                data2.sortKey = "timestamp"

                HttpApi().getAuctionProduct(1,-1,auth.currentUser!!.uid,"timestamp").enqueue(object : Callback<ProductAuctionDTO.ProductResponseDTO>{
                    override fun onResponse(
                        call: Call<ProductAuctionDTO.ProductResponseDTO>,
                        response: Response<ProductAuctionDTO.ProductResponseDTO>
                    ) {
                        println("게시글 조회 결과 ${response.body()!!.toString()}")

                        response.body()!!.data.forEach {
                            println("내부 데이터 ${it.title}")
                        }

                    }

                    override fun onFailure(call: Call<ProductAuctionDTO.ProductResponseDTO>, t: Throwable) {
                        println("실패")
                    }

                })
            }

            fragmentAuctionBackground.setOnClickListener {
                clickFab()
                fragmentAuctionBackground.setBackgroundResource(R.color.colorTransparent)
                fragmentAuctionBackground.visibility = View.GONE
            }

            //옥션 버튼
            fragmentAuctionButtonAuction.setOnClickListener {

            }

            //거래 버튼
            fragmentAuctionButtonTrade.setOnClickListener {

            }
            fragmentAuctionRecyclerview.adapter = AuctionAdapter(binding.root.context,auctionData,auctionDataId)
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