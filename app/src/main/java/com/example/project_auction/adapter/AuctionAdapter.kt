package com.example.project_auction.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.databinding.ItemAuctionBinding
import com.example.project_auction.util.time.TimeUtil
import com.example.project_auction.view.activity.detailproduct.DetailAuctionActivity

class AuctionAdapter(val context: Context,val dataList : ArrayList<ProductAuctionDTO>, val dataIdList : ArrayList<String>) : RecyclerView.Adapter<AuctionViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionViewHolder {
        val binding = ItemAuctionBinding.inflate(LayoutInflater.from(context),parent,false)
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
        holder.binding.itemAuctionTextviewStartCost.text = "경매 시작가 " + dataList[position].startCost + "원"

        //좋아요 갯수
        holder.binding.itemAuctionTextviewFavorite.text = dataList[position].favoriteCount.toString()

        //상품 자체 클릭
        holder.itemView.setOnClickListener {

            var intent = Intent(holder.binding.root.context,DetailAuctionActivity::class.java)
            intent.apply {
                putExtra("productData", dataList[position])
                holder.binding.root.context.startActivity(this)
            }


        }
        
        //좋아요 클릭
        favorite(dataIdList[position])

        //종료 시간
        holder.binding.itemAuctionTextviewClosetime.text = TimeUtil().formatCloseTimeString(dataList[position].closeTimestamp!!.toLong())

        //조회수
        holder.binding.itemAuctionTextviewViewcount.text = dataList[position].viewCount.toString()
    }

    override fun getItemCount(): Int {
        if (dataList != null){
            return dataList.size
        }else{
            return 0
        }
    }

    fun favorite(postId : String){

    }


}

class AuctionViewHolder(val binding : ItemAuctionBinding) : RecyclerView.ViewHolder(binding.root){
    fun onBind(data : ProductAuctionDTO){
        binding.itemauction = data
    }
}