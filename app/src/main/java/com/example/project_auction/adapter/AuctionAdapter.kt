package com.example.project_auction.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.databinding.ItemAuctionBinding
import com.example.project_auction.repository.ProductCollectionRepository
import com.example.project_auction.util.time.TimeUtil
import com.example.project_auction.view.activity.detailproduct.DetailAuctionActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class AuctionAdapter(
    val context: Context,
    val dataList: ArrayList<ProductAuctionDTO>,
    val dataIdList: ArrayList<String>
) : RecyclerView.Adapter<AuctionViewHolder>() {

    val coroutineScopeMain = CoroutineScope(Dispatchers.Main)
    val productRepository = ProductCollectionRepository()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionViewHolder {
        val binding = ItemAuctionBinding.inflate(LayoutInflater.from(context), parent, false)
        return AuctionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuctionViewHolder, position: Int) {
        holder.onBind(dataList[position])

        //상품명
        holder.binding.itemAuctionTextviewTitle.text = dataList[position].title

        //사진
        Glide.with(context)
            .load(dataList[position].photoList!![0])
            .centerCrop()
            .thumbnail(0.1f)
            .into(holder.binding.itemAuctionImageviewPhoto)
        holder.binding.itemAuctionImageviewPhoto.clipToOutline = true

        //카테고리
        holder.binding.itemAuctionTextviewCategory.text = dataList[position].category

        //시작가
        holder.binding.itemAuctionTextviewStartCost.text =
            "경매 시작가 " + DecimalFormat("#,###").format(dataList[position].startCost!!.toLong()) + "원"

        //좋아요 갯수
        //holder.binding.itemAuctionTextviewFavorite.text = dataList[position].favoriteCount.toString()

        //상품 자체 클릭
        holder.itemView.setOnClickListener {

            var intent = Intent(holder.binding.root.context, DetailAuctionActivity::class.java)
            intent.apply {
                putExtra("productData", dataList[position])
                putExtra("productId", dataIdList[position])
                holder.binding.root.context.startActivity(this)
            }


        }

        //좋아요 클릭
        holder.binding.itemAuctionImagebuttonFavorite.setOnClickListener {
            favorite(dataIdList[position], holder)
        }

        //좋아요 체크
        checkFavorite(dataIdList[position], holder)

        //종료 시간
        if (((dataList[position].closeTimestamp!!.toLong()) - System.currentTimeMillis()) <= 0) {
            holder.binding.itemAuctionTextviewClosetime.text = "경매 종료"
        } else {
            holder.binding.itemAuctionTextviewClosetime.text = TimeUtil().formatCloseTimeString((dataList[position].closeTimestamp!!.toLong() - System.currentTimeMillis()))
        }

        //현재 경매가
        holder.binding.itemAuctionTextviewCurrentCost.text = "현재 경매가 : " + DecimalFormat("#,###").format(dataList[position].currentCost!!.toLong()) +"원"



        //조회수(경매참여자수)
        holder.binding.itemAuctionTextviewViewcount.text = "경매 참여자 : " +dataList[position].joinCount.toString()
    }

    override fun getItemCount(): Int {
        if (dataList != null) {
            return dataList.size
        } else {
            return 0
        }
    }

    //좋아요 액션
    fun favorite(postId: String, holder: AuctionViewHolder) {

        coroutineScopeMain.launch {
            productRepository.updateFavorite(postId, auth.currentUser!!.uid).collect {
                if (it) {
                    checkFavorite(postId, holder)
                } else println("실패")
            }
        }
    }

    //좋아요 체크
    fun checkFavorite(postId: String, holder: AuctionViewHolder) {
        coroutineScopeMain.launch {
            productRepository.checkFavorite(postId, auth.currentUser!!.uid).collect {

                if (it) {
                    holder.binding.itemAuctionImagebuttonFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    holder.binding.itemAuctionImagebuttonFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
                }

            }
        }
    }


}

class AuctionViewHolder(val binding: ItemAuctionBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: ProductAuctionDTO) {
        binding.itemauction = data
    }
}