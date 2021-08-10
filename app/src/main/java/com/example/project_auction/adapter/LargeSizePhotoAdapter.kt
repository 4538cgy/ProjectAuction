package com.example.project_auction.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_auction.databinding.ItemLargeSizePhotoBinding
import com.example.project_auction.view.activity.photo.PhotoViewActivity

class LargeSizePhotoAdapter (val context : Context, val photoList : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemLargeSizePhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LargeSizeViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as LargeSizeViewHolder)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Glide.with(holder.itemView.context)
                .load(photoList[position])
                .centerCrop()
                .thumbnail(0.1f)
                .into(viewHolder.binding.itemLargeSizePhotoImageview)
        }
        viewHolder.binding.itemLargeSizePhotoImageview.setOnClickListener {
            //photoview activity 열리게
            var intent = Intent(viewHolder.binding.root.context,PhotoViewActivity::class.java)
            intent.apply {
                putExtra("photoUri",photoList[position])
                viewHolder.binding.root.context.startActivity(intent)
            }

        }

    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}

class LargeSizeViewHolder(var binding: ItemLargeSizePhotoBinding) : RecyclerView.ViewHolder(binding.root){
    fun onBind(){

    }
}