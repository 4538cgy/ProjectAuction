package com.example.project_auction.view.activity.addpost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivityAddAuctionPostBinding

class AddAuctionPostActivity : BaseActivity<ActivityAddAuctionPostBinding>(R.layout.activity_add_auction_post) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activityaddauctionpost = this

        binding.apply {
            activityAddAuctionPostImagebuttonClose.setOnClickListener {
                finish()
            }

            activityAddAuctionPostEdittextTitle.addTextChangedListener {
                activityAddAuctionPostTextviewTitle.text = it!!.length.toString() + "/24"
            }

            activityAddAuctionPostEdittextProductIntro.addTextChangedListener {
                activityAddAuctionPostTextviewProductIntroCount.text = it!!.length.toString() + "/400"
            }

            activityAddAuctionPostButtonPhotoAdd.setOnClickListener {
                //사진 추가
            }

            activityAddAuctionPostTextviewCategory.setOnClickListener {
                //카테고리 설정
            }

            activityAddAuctionPostTextviewCloseTime.setOnClickListener {
                //경재 기간 설정
            }

            activityAddAuctionPostTextinputlayoutStartCost.setOnClickListener {
                //경매 시작가 설정
            }

            activityAddAuctionPostTextinputlayoutLastCost.setOnClickListener {
                //즉시 입찰가 설정
            }
        }
    }
}