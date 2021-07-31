package com.example.project_auction.view.fragment.lobby.alarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_auction.R
import com.example.project_auction.adapter.AlarmAdapter
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.data.AlarmDTO
import com.example.project_auction.databinding.FragmentAlarmBinding


class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {

    var alarmList = arrayListOf<AlarmDTO>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentalarm = this
        binding.lifecycleOwner = this



        initRecyclerView()
        loadAlarmData()
        alarmViewModel.alarmDatas.observe(viewLifecycleOwner, Observer {
            alarmList.clear()
            it.forEach {
                alarmList.add(it)
            }
            println("???? ${alarmList.toString()}")
            binding.fragmentAlarmRecyclerview.adapter!!.notifyDataSetChanged()
            if (alarmList.isEmpty()) binding.fragmentAlarmTextviewNotice.visibility = View.VISIBLE
        })

    }


    fun loadAlarmData(){
        alarmViewModel.loadAlarmDatas(auth.currentUser!!.uid)
    }

    fun initRecyclerView(){
        binding.fragmentAlarmRecyclerview.adapter = AlarmAdapter(binding.root.context,alarmList)
        binding.fragmentAlarmRecyclerview.layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)
    }
}