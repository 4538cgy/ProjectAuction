package com.example.project_auction.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.view.activity.photo.PhotoViewActivity
import kotlinx.android.synthetic.main.item_large_size_photo.view.*
import kotlinx.android.synthetic.main.item_photo.view.*

class LargeSizePhotoAdapter (val context : Context, val photoList : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_large_size_photo, parent, false)
        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as CustomViewHolder).itemView

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Glide.with(holder.itemView.context)
                .load(photoList[position])
                .centerCrop()
                .thumbnail(0.1f)
                .into(viewHolder.item_large_size_photo_imageview)
        }
        viewHolder.item_large_size_photo_imageview.setOnClickListener {
            //photoview activity 열리게
            var intent = Intent(viewHolder.context,PhotoViewActivity::class.java)
            intent.apply {
                putExtra("photoUri",photoList[position])
                viewHolder.context.startActivity(intent)
            }

        }

    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}