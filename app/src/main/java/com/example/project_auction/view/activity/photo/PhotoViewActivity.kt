package com.example.project_auction.view.activity.photo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivityPhotoViewBinding

class PhotoViewActivity : BaseActivity<ActivityPhotoViewBinding>(R.layout.activity_photo_view) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activityphotoview = this

        binding.apply {
            Glide.with(root.context)
                .load(intent.getStringExtra("photoUri"))
                .centerCrop()
                .thumbnail(0.1f)
                .into(activityPhotoView)

            activityPhotoViewImagebuttonClose.setOnClickListener {
                finish()
            }
        }

    }
}