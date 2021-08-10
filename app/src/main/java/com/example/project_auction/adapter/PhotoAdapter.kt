package com.example.project_auction.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_auction.databinding.ItemPhotoBinding

class PhotoAdapter(val context : Context,val photoList : ArrayList<String>,val deleteType : Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PhotoViewHolder(view)
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as PhotoViewHolder)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Glide.with(holder.itemView.context)
                .load(photoList[position])
                .centerCrop()
                .thumbnail(0.1f)
                .into(viewHolder.binding.itemPhotoImageviewPhoto)
        }

        if (deleteType) viewHolder.binding.itemPhotoImagebuttonClose.visibility = View.VISIBLE

        viewHolder.binding.itemPhotoConstAll.setOnClickListener {
            if (deleteType) {
                photoList.removeAt(position)

                notifyDataSetChanged()
            }
        }

        viewHolder.binding.itemPhotoImagebuttonClose.setOnClickListener {
            if (deleteType) {
                photoList.removeAt(position)

                notifyDataSetChanged()
            }
        }

    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}

class PhotoViewHolder(var binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root){
}