package com.example.project_auction.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.data.ProductTradeDTO
import com.example.project_auction.databinding.ItemTradeBinding
import com.example.project_auction.repository.ProductCollectionRepository
import com.example.project_auction.view.activity.detailproduct.DetailTradeActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class TradeAdapter(val context: Context,
val dataList: ArrayList<ProductTradeDTO>,
val dataIdList: ArrayList<String>
) : RecyclerView.Adapter<TradeViewHolder>() {

    val coroutineScopeMain = CoroutineScope(Dispatchers.Main)
    val productRepository = ProductCollectionRepository()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeViewHolder {
        val binding = ItemTradeBinding.inflate(LayoutInflater.from(context), parent, false)

        val width = (binding.root.resources.displayMetrics.widthPixels / 3) - 40
        var imageView = binding.itemTradeImageviewPhoto
        imageView.layoutParams = ConstraintLayout.LayoutParams(width, width)

        return TradeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TradeViewHolder, position: Int) {
        holder.onBind(dataList[position])


        //사진
        Glide.with(context)
            .load(dataList[position].photoList!![0])
            .centerCrop()
            .thumbnail(0.1f)
            .into(holder.binding.itemTradeImageviewPhoto)
        holder.binding.itemTradeImageviewPhoto.clipToOutline = true

        //상품명
        holder.binding.itemTradeTextviewTitle.text = dataList[position].title

        //가격
        holder.binding.itemTradeTextviewCost.text = DecimalFormat("#,###").format(dataList[position].cost!!.toString().replace(",","").toLong()) + "원"

        //좋아요 갯수
        holder.binding.itemTradeTextviewFavoriteCount.text = dataList[position].favoriteCount.toString()

        //좋아요 액션
        holder.binding.itemTradeImagebuttonFavorite.setOnClickListener {
            favorite(dataIdList[position],holder)
        }

        //좋아요 체크
        checkFavorite(dataIdList[position],holder)

        //아이템 클릭
        holder.itemView.setOnClickListener {
            var intent = Intent(holder.binding.root.context,DetailTradeActivity::class.java)
            holder.binding.root.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        if (dataList != null) {
            return dataList.size
        } else {
            return 0
        }
    }

    //좋아요 액션
    fun favorite(postId: String, holder: TradeViewHolder) {

        coroutineScopeMain.launch {
            productRepository.updateFavoriteTradePost(postId, auth.currentUser!!.uid).collect {
                if (it) {
                    checkFavorite(postId, holder)
                } else println("실패")
            }
        }
    }

    //좋아요 체크
    fun checkFavorite(postId: String, holder: TradeViewHolder) {
        coroutineScopeMain.launch {
            productRepository.checkFavoriteTradePost(postId, auth.currentUser!!.uid).collect {

                if (it) {
                    holder.binding.itemTradeImagebuttonFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_white_24)
                } else {
                    holder.binding.itemTradeImagebuttonFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_white_24)
                }



            }
        }
    }


}

class TradeViewHolder(val binding: ItemTradeBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: ProductTradeDTO) {
        binding.itemtrade = data
    }


}