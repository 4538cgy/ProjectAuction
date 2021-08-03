package com.example.project_auction.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.data.*
import com.example.project_auction.databinding.*
import com.example.project_auction.util.time.TimeUtil
import com.example.project_auction.view.activity.detailproduct.DetailAuctionActivity

class HistoryAdapter(private val items : ArrayList<HistoryItemDTO>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val auction = 0
    val trade = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            auction -> {
                return AuctionViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_history_auction,parent,false))
            }
            else -> {
                return TradeViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_history_trade,parent,false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (items[position].type == HistoryType.auction){
            (holder as AuctionViewHolder).bind(items[position].auctions!!)
            holder.binding.itemHistoryAuctionTextviewTitle.text = items[position].auctions!!.title
            holder.binding.itemHistoryAuctionTextviewTimestamp.text = TimeUtil().formatTimeString(items[position].auctions!!.timestamp!!)
            holder.binding.itemHistoryAuctionTextviewCurrentCost.text = "현재 가격 "+items[position].auctions!!.currentCost + "원"
            holder.binding.itemHistoryAuctionTextviewCurrentJoinCount.text = "경매 참여자 "+items[position].auctions!!.joinCount +"명"
            Glide.with(holder.binding.root.context)
                .load(items[position].auctions!!.photoList!![0])
                .centerCrop()
                .thumbnail(0.1f)
                .into(holder.binding.itemHistoryAuctionImageviewImage)
            holder.binding.itemHistoryAuctionImageviewImage.clipToOutline = true
            holder.itemView.setOnClickListener {
                var intent = Intent(holder.binding.root.context,DetailAuctionActivity::class.java)
                intent.apply {
                    putExtra("productId", items[position].auctionsId)
                    it.context.startActivity(intent)
                }
            }
        }else{
            (holder as TradeViewHolder).bind(items[position].trades!!)
            holder.binding.itemHistoryTradeTextviewTitle.text = items[position].trades!!.title
            holder.binding.itemHistoryTradeTextviewTimestamp.text = TimeUtil().formatTimeString(items[position].trades!!.timestamp!!)
            holder.binding.itemHistoryTradeTextviewState.text = when(items[position].trades!!.tradeState){
                0 ->{ "판매 중" }
                1 ->{ "예약 중"}
                2 ->{ "판매 완료"}
                else -> return
            }
            holder.binding.itemHistoryTradeTextviewFavoriteCount.text = items[position].trades!!.favoriteCount.toString()

            Glide.with(holder.binding.root.context)
                .load(items[position].trades!!.photoList!![0])
                .centerCrop()
                .thumbnail(0.1f)
                .into(holder.binding.itemHistoryTradeImageview)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = if(items[position].type == HistoryType.auction) auction  else trade

    inner class TradeViewHolder(val binding : ItemHistoryTradeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ProductTradeDTO){
            binding.apply {
                itemhistorytrade = item
            }
        }
    }
    inner class AuctionViewHolder(val binding : ItemHistoryAuctionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ProductAuctionDTO){
            binding.apply {
                itemhistoryauction = item

            }
        }
    }
}