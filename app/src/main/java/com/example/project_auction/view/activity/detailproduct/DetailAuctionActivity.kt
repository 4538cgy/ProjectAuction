package com.example.project_auction.view.activity.detailproduct

import android.icu.util.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_auction.R
import com.example.project_auction.adapter.LargeSizePhotoAdapter
import com.example.project_auction.adapter.PhotoAdapter
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.databinding.ActivityDetailAuctionBinding
import com.example.project_auction.util.time.TimeUtil

class DetailAuctionActivity : BaseActivity<ActivityDetailAuctionBinding>(R.layout.activity_detail_auction) {

    lateinit var data : ProductAuctionDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activitydetailauction = this

        data = intent.getSerializableExtra("productData") as ProductAuctionDTO


        binding.apply { 
            //뒤로가기
            activityDetailAuctionImagebuttonClose.setOnClickListener { 
                finish()
            }
            //상품 제목
            activityDetailAuctionTextviewTitle.text = data.title

            //카테고리
            activityDetailAuctionTextviewCategory.text = data.category

            //게시시간
            activityDetailAuctionTextviewTime.text = TimeUtil().formatTimeString(data.timestamp!!.toLong()).toString()

            //상품 설명
            activityDetailAuctionTextviewExplain.text = data.content

            //뷰페이저
            activityDetailAuctionViewpager.adapter = LargeSizePhotoAdapter(binding.root.context,
                data.photoList!!
            )
            //인티케이터
            activityDetailAuctionIndicator.setViewPager(activityDetailAuctionViewpager)

            //닉네임

            //프로필 이미지

            //경매 종료시간

            //경매 참여 버튼

            //현재 참여자

            //옵션 버튼튼

        }    }

    private fun getUserNickName(){

    }
}