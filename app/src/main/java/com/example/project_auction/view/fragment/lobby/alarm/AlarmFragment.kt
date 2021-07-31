package com.example.project_auction.view.fragment.lobby.alarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_auction.R
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.databinding.FragmentAlarmBinding


class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentalarm = this
        binding.lifecycleOwner = this



        initRecyclerView()
        loadAlarmData()

        alarmViewModel.alarmDatas.observe(viewLifecycleOwner, Observer {
            
        })
    }

    fun loadAlarmData(){
        alarmViewModel.loadAlarmDatas(auth.currentUser!!.uid)
    }

    fun initRecyclerView(){
        binding.fragmentAlarmRecyclerview.adapter
        binding.fragmentAlarmRecyclerview.layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)
        alarmViewModel.loadAlarmDatas(auth.currentUser!!.uid)
    }
}