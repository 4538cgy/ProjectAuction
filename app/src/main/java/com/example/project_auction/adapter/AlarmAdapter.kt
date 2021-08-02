package com.example.project_auction.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_auction.data.AlarmDTO
import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.databinding.ItemAlarmBinding
import com.example.project_auction.databinding.ItemAuctionBinding
import com.example.project_auction.util.time.TimeUtil

class AlarmAdapter(val context : Context, val alarmList : ArrayList<AlarmDTO>) : RecyclerView.Adapter<AlarmViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(context),parent,false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.onBind(alarmList[position])

        println("아아아아 데이터가있어요~!")

        holder.binding.itemAlarmImageview

        holder.binding.itemAlarmTextviewMessage.text = alarmList[position].message.toString()

        holder.binding.itemAlarmTextviewTimestamp.text = TimeUtil().formatTimeString(alarmList[position].timestamp!!)
    }

    override fun getItemCount(): Int {
        if (alarmList != null){
            return alarmList.size
        }else{
            return 0
        }
    }
}

class AlarmViewHolder(val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: AlarmDTO) {
        binding.itemalarm = data
    }
}