package com.example.project_auction.view.activity.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivityEditProfileBinding
import com.example.project_auction.extension.toast

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>(R.layout.activity_edit_profile) {

    private val profileCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it != null) {
            if (it.data != null){
                photoUri = it.data!!.data.toString()
                Glide.with(binding.root.context)
                    .load(it.data!!.data)
                    .circleCrop()
                    .into(binding.activityEditProfileImageviewPhoto)


                profileViewModel.updateProfileImage.postValue(it.data!!.data)
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
                updateProfile()
                activityEditProfileContainerLoading.visibility = View.VISIBLE
                activityEditProfileButtonComplete.visibility = View.GONE
            }

            //이미지 뷰 클릭
            activityEditProfileImageviewPhoto.setOnClickListener {
                openGallery()
            }

            //텍스트 수 체크
            activityEditProfileEdittextNick.addTextChangedListener {
                activityEditProfileTextviewNickCount.text = it!!.length.toString() + "/12"
                if (it!!.length > 2){
                    activityEditProfileButtonVerifyNick.visibility = View.VISIBLE
                }else{
                    activityEditProfileButtonVerifyNick.visibility = View.GONE
                }
            }

            //닉네임 중복 체크
            activityEditProfileButtonVerifyNick.setOnClickListener {
                nickCheck(activityEditProfileEdittextNick.text.toString())
            }



        }

        //닉네임 중복 체크 관찰
        profileViewModel.duplicateCheck.observe(this, Observer {

            println("으아아아아아 $it")
            if (it) {
                toast("해당 닉네임이 이미 사용 중 입니다.")
            }else{
                var builder = AlertDialog.Builder(binding.root.context)
                builder.apply {
                    setMessage("닉네임이 존재하지 않습니다. 해당 닉네임으로 진행하시겠습니까?")
                    setPositiveButton("예" , DialogInterface.OnClickListener { dialog, which ->
                        binding.activityEditProfileButtonComplete.apply {
                            setBackgroundResource(R.drawable.background_round_yellow_24dp)
                            isEnabled = true
                        }
                        binding.activityEditProfileEdittextNick.isEnabled = false
                    })
                    setNegativeButton("아니요" , DialogInterface.OnClickListener { dialog, which ->

                    })
                    setTitle("안내")
                    show()
                }
            }
        })

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


        //프로필 이미지 업로드 확인
        profileViewModel.profileUploadCheck.observe(this , Observer {
            if (it) updateNickname()
        })

        //닉네임까지 변경됬는지 확인
        profileViewModel.updateUserNicknameCheck.observe(this, Observer {
            if (it) {
                finish()
                binding.activityEditProfileContainerLoading.visibility = View.GONE
                binding.activityEditProfileButtonComplete.visibility = View.VISIBLE
            }
        })
    }

    fun nickCheck(nickName : String){
        profileViewModel.nickNameDuplicatedCheck(nickName)
    }

    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        profileCallback.launch(intent)
    }

    fun updateProfile(){

        if (profileViewModel.updateProfileImage.value != null)
        profileViewModel.uploadProfileImage(auth.currentUser!!.uid, Uri.parse(profileViewModel.updateProfileImage.value.toString()) )
        else{
            updateNickname()
        }

    }

    fun updateNickname(){
        profileViewModel.updateUserNickName(auth.currentUser!!.uid,binding.activityEditProfileEdittextNick.text.toString())
    }

}