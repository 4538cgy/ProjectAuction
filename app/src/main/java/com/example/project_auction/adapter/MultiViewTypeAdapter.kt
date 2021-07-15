package com.example.project_auction.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_auction.R
import com.example.project_auction.data.ViewTypeModelDTO

class MultiViewTypeAdapter(private val list: List<ViewTypeModelDTO>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when (viewType) {
            ViewTypeModelDTO.FIRST_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_type_first, parent, false)
                FirstTypeViewHolder(view)
            }
            ViewTypeModelDTO.SECOND_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_type_second, parent, false)
                SecondTypeViewHolder(view)
            }
            else -> throw RuntimeException("Unknown view type error!!")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val obj = list[position]
        when (obj.type) {
            ViewTypeModelDTO.FIRST_TYPE -> {
                (holder as FirstTypeViewHolder).txtType.text = obj.text
                holder.content.text = obj.contentString
                holder.image.setImageResource(obj.data)
            }
            ViewTypeModelDTO.SECOND_TYPE -> {
                (holder as SecondTypeViewHolder).txtType.text = obj.text
            }
        }
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    inner class FirstTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtType: TextView = itemView.findViewById(R.id.recyclerview_type_first_textview_title)
        val content: TextView = itemView.findViewById(R.id.recyclerview_type_first_textview_content)
        val image: ImageView = itemView.findViewById(R.id.recyclerview_type_first_imageview_image)
    }

    inner class SecondTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtType: TextView = itemView.findViewById(R.id.recyclerview_type_second_textview_item)
    }
}