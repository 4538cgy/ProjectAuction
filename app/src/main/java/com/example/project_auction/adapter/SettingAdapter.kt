package com.example.project_auction.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_auction.data.SettingItem
import com.example.project_auction.data.SettingType

class SettingAdapter(private val items : ArrayList<SettingItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val title = 0
    private val item = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            title -> {

            }
            else -> {

            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (items[position].type == SettingType.TITLE){

        }else{

        }
    }

    override fun getItemCount() = items.size

    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = if(items[position].type == SettingType.TITLE) title  else item
}