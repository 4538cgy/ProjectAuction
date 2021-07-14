package com.example.project_auction.view.activity.history

import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_auction.R
import com.example.project_auction.adapter.MultiViewTypeAdapter
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.data.ViewTypeModelDTO
import com.example.project_auction.databinding.ActivityRegistrationHistoryBinding

class RegistrationHistoryActivity : BaseActivity<ActivityRegistrationHistoryBinding>(R.layout.activity_registration_history) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            activityregistrationhistory = this@RegistrationHistoryActivity
        }

        setSupportActionBar(binding.activityRegistrationHistoryToolbarTitle)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);

        val list = listOf(
            ViewTypeModelDTO(ViewTypeModelDTO.FIRST_TYPE, "Title Test 0 !!", R.drawable.ic_baseline_supervised_user_circle_24, "ContentString Test 0 !!"),
            ViewTypeModelDTO(ViewTypeModelDTO.FIRST_TYPE, "Title Test 1 !!", R.drawable.ic_baseline_close_24, "ContentString Test 1 !!"),
            ViewTypeModelDTO(ViewTypeModelDTO.FIRST_TYPE, "Title Test 2 !!", R.drawable.ic_baseline_add_photo_alternate_24, "ContentString Test 2 !!"),
            ViewTypeModelDTO(ViewTypeModelDTO.FIRST_TYPE, "Title Test 3 !!", R.drawable.ic_baseline_add_24, "ContentString Test 3 !!"),
            ViewTypeModelDTO(ViewTypeModelDTO.FIRST_TYPE, "Title Test 4 !!", R.drawable.ic_baseline_supervised_user_circle_24, "ContentString Test 4 !!"),
            ViewTypeModelDTO(ViewTypeModelDTO.FIRST_TYPE, "Title Test 5 !!", R.drawable.ic_baseline_add_24, "ContentString Test 5 !!"),
            ViewTypeModelDTO(ViewTypeModelDTO.FIRST_TYPE, "Title Test 6 !!", 0, "ContentString Test 6 !!"),
            ViewTypeModelDTO(ViewTypeModelDTO.FIRST_TYPE, "Title Test 7 !!", R.drawable.ic_baseline_add_24, "ContentString Test 7 !!"),
            ViewTypeModelDTO(ViewTypeModelDTO.FIRST_TYPE, "Title Test 8 !!", R.drawable.ic_baseline_add_photo_alternate_24, "ContentString Test 8 !!")
        )

        val adpater = MultiViewTypeAdapter(list)
        binding.activityRegistrationHistoryRecyclerviewItem.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.activityRegistrationHistoryRecyclerviewItem.adapter = adpater
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