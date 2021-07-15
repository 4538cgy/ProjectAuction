package com.example.project_auction.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.databinding.ItemAuctionBinding

class AuctionAdapter(val context: Context,val dataList : ArrayList<ProductAuctionDTO>) : RecyclerView.Adapter<AuctionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionViewHolder {
        val binding = ItemAuctionBinding.inflate(LayoutInflater.from(context),parent,false)
        return AuctionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuctionViewHolder, position: Int) {
        holder.onBind(dataList[position])

        holder.binding.itemAuctionTextviewTitle.text = dataList[position].title

        Glide.with(context)
            .load(dataList[position].photoList!![0])
            .into(holder.binding.itemAuctionImageviewPhoto)

        holder.binding.itemAuctionTextviewCategory.text = dataList[position].category

        holder.binding.itemAuctionTextviewStartCost.text = dataList[position].startCost

        holder.binding.itemAuctionTextviewFavorite.text = dataList[position].favoriteCount.toString()

    }

    override fun getItemCount(): Int {
        if (dataList != null){
            return dataList.size
        }else{
            return 0
        }
    }


}

class AuctionViewHolder(val binding : ItemAuctionBinding) : RecyclerView.ViewHolder(binding.root){
    fun onBind(data : ProductAuctionDTO){
        binding.itemauction = data
    }
}