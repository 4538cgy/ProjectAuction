package com.example.project_auction.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.project_auction.R
import com.example.project_auction.data.SettingItem
import com.example.project_auction.data.SettingType
import com.example.project_auction.databinding.ItemLayoutSettingBinding

class SettingAdapter(private val items : ArrayList<SettingItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var listener: OnSettingItemClickListener? = null
    private val title = 0
    private val item = 1

    interface OnSettingItemClickListener {
        fun onItemClick(item: SettingItem)
    }

    fun setOnSettingItemClickListener(action: SettingItem.() -> Unit): SettingAdapter {
        listener = object : OnSettingItemClickListener {
            override fun onItemClick(item: SettingItem) {
                action(item)
            }
        }
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            title -> {
                return TitleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout_title,parent,false))
            }
            else -> {
                return ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_layout_setting,parent,false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (items[position].type == SettingType.TITLE){
            (holder as TitleViewHolder)
        }else{
            (holder as ItemViewHolder).bind(items[position])
            holder.binding.itemLayoutSettingImageview.setImageResource(items[position].icon!!)
            holder.binding.itemLayoutSettingTextview.text = items[position].title
        }
    }

    override fun getItemCount() = items.size

    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = if(items[position].type == SettingType.TITLE) title  else item

    inner class TitleViewHolder(view : View) : RecyclerView.ViewHolder(view){

    }
    inner class ItemViewHolder(val binding : ItemLayoutSettingBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : SettingItem){
            binding.apply {
                itemlayoutsetting = item
                root.setOnClickListener {
                    listener?.onItemClick(item)
                }
            }
        }
    }
}