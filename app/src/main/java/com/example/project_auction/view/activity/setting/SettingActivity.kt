package com.example.project_auction.view.activity.setting

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_auction.R
import com.example.project_auction.adapter.MultiViewTypeAdapter
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.data.ViewTypeModelDTO
import com.example.project_auction.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            activitysetting = this@SettingActivity
        }

        setSupportActionBar(binding.activitySettingToolbarTitle)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);

        val list = listOf(
            ViewTypeModelDTO(ViewTypeModelDTO.SECOND_TYPE, "공지사항", 0, null),
            ViewTypeModelDTO(ViewTypeModelDTO.SECOND_TYPE, "버전정보", 0, null),
            ViewTypeModelDTO(ViewTypeModelDTO.SECOND_TYPE, "알림", 0, null),
            ViewTypeModelDTO(ViewTypeModelDTO.SECOND_TYPE, "테마", 0, null),
            ViewTypeModelDTO(ViewTypeModelDTO.SECOND_TYPE, "기타", 0, null)
        )

        val dividerItemDecoration =
            DividerItemDecoration(
                binding.activitySettingRecyclerviewItem.context,
                LinearLayoutManager(this).orientation
            )
        binding.activitySettingRecyclerviewItem.addItemDecoration(dividerItemDecoration)

        val adpater = MultiViewTypeAdapter(list)
        binding.activitySettingRecyclerviewItem.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adpater.setItemClickListener(object : MultiViewTypeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val item = list[position]

                Toast.makeText(v.context, "Click = ${item.text}", Toast.LENGTH_SHORT).show()
                adpater.notifyDataSetChanged()
            }
        })
        binding.activitySettingRecyclerviewItem.adapter = adpater
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}