package com.example.project_auction.view.activity.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivityEditProfileBinding

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>(R.layout.activity_edit_profile) {

    private val profileCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it != null) {
            if (it.data != null){
                photoUri = it.data!!.data.toString()
                Glide.with(binding.root.context)
                    .load(it.data!!.data)
                    .circleCrop()
                    .into(binding.activityEditProfileImageviewPhoto)

                loginSignViewModel.profilePhotoUri.postValue(it.data!!.data)
            }
        }
    }
    lateinit var photoUri : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            activityeditprofile = this@EditProfileActivity
            profileviewmodel = profileViewModel
            lifecycleOwner = this@EditProfileActivity
            
            //뒤로가기
            activityEditProfileImagebuttonClose.setOnClickListener { 
                finish()
            }

            //저장
            activityEditProfileButtonComplete.setOnClickListener {
                
            }

            //이미지 뷰 클릭
            activityEditProfileImageviewPhoto.setOnClickListener {
                openGallery()
            }

            //텍스트 수 체크
            activityEditProfileEdittextNick.addTextChangedListener {
                activityEditProfileTextviewNickCount.text = it!!.length.toString() + "/12"
            }

        }

        //프로필 이미지
        profileViewModel.getProfileImage(auth.currentUser!!.uid)
        profileViewModel.profileImage.observe(this@EditProfileActivity, Observer {
            Glide.with(binding.root.context)
                .load(it)
                .circleCrop()
                .thumbnail(0.1f)
                .into(binding.activityEditProfileImageviewPhoto)
        })
        //닉네임
        profileViewModel.getUserNickName(auth.currentUser!!.uid)
        profileViewModel.nickName.observe(this@EditProfileActivity, Observer {
            binding.activityEditProfileEdittextNick.setText(it.toString())
        })
    }

    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        profileCallback.launch(intent)
    }

}