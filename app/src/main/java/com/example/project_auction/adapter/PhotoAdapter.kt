package com.example.project_auction.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_auction.R
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoAdapter(val context : Context,val photoList : ArrayList<String>,val deleteType : Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
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
                .into(viewHolder.item_photo_imageview_photo)
        }

        if (deleteType) viewHolder.item_photo_imagebutton_close.visibility = View.VISIBLE

        viewHolder.item_photo_const_all.setOnClickListener {
            if (deleteType) {
                photoList.removeAt(position)

                notifyDataSetChanged()
            }
        }

        viewHolder.item_photo_imagebutton_close.setOnClickListener {
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