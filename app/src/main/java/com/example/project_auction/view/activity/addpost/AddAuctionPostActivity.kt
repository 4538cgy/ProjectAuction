package com.example.project_auction.view.activity.addpost

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivityAddAuctionPostBinding
import com.example.project_auction.extension.toast
import com.google.android.gms.auth.api.Auth

class AddAuctionPostActivity : BaseActivity<ActivityAddAuctionPostBinding>(R.layout.activity_add_auction_post) {

    private var photoList = arrayListOf<String>()

    private val addPhotoCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val clipCount = it.data!!.clipData!!.itemCount
        var currentItem = 0
        if (it != null){
            if (it.data != null){
                if (it.data!!.clipData!!.itemCount < 2){
                    photoList.add(it.data!!.data.toString())
                }else{
                    while (currentItem < clipCount) {
                        val imageUri =
                            it.data!!.clipData!!.getItemAt(currentItem).uri
                        photoList.add(imageUri.toString())
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        currentItem += 1
                    }
                }
            }
        }
    }

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
                addPhoto()
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

    fun addPhoto(){

        toast("사진을 꾹 누르시면 여러장을 선택할 수 있습니다.")

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        }

        addPhotoCallback.launch(intent)
    }
}