package com.example.project_auction.view.activity.signup

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.project_auction.R
import com.example.project_auction.adapter.SignUpViewPagerAdapter
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.data.NickNameDTO
import com.example.project_auction.data.UserDTO
import com.example.project_auction.databinding.ActivitySignUpBinding
import com.example.project_auction.extension.toast
import com.example.project_auction.view.activity.lobby.LobbyActivity
import com.example.project_auction.view.fragment.signup.SignUpFirstFragment
import com.example.project_auction.view.fragment.signup.SignUpSecondFragment
import com.example.project_auction.view.fragment.signup.SignUpThirdFragment
import com.example.project_auction.viewmodel.LoginSignUpViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {

    private val viewModel: LoginSignUpViewModel by viewModels()
    private var signUpFinishState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            activitysignup = this@SignUpActivity
            loginsignupviewmodel = viewModel

            val fragmentList = arrayListOf<Fragment>(
                SignUpFirstFragment(),
                SignUpSecondFragment(),
                SignUpThirdFragment()
            )
            val adapter = SignUpViewPagerAdapter(this@SignUpActivity)
            adapter.fragments = fragmentList
            activitySignUpViewpager2.apply {
                this.adapter = adapter
                this.run { isUserInputEnabled = false }
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        updateView(position)
                    }
                })
            }

            activitySignUpButtonNext.setOnClickListener {
                activitySignUpViewpager2.currentItem = activitySignUpViewpager2.currentItem + 1

                if (signUpFinishState) {
                    //회원 정보 저장
                    binding.groupAll.visibility = View.GONE
                    uploadProfileData()
                    binding.activitySignUpProgressbar.visibility = View.VISIBLE
                    binding.activitySignUpTextviewLoading.visibility = View.VISIBLE
                }
            }


        }

        //유저 데이터셋이 업로도 됬는지 관찰 하고 true면 화면 이동
        profileViewModel.userDataSetUploadCheck.observe(this, Observer {
            if (it) {
                startActivity(Intent(binding.root.context, LobbyActivity::class.java))
                finish()
            }else{
                toast("회원 정보 등록 실패")
            }
        })
        //프로필 이미지 업로드 되는지 관찰 하고 true면 데이터셋 업로드
        profileViewModel.profileUploadCheck.observe(this, Observer {
            if (it) userDataSave() else toast("유저 등록에 실패하였습니다.")
        })

        //프로필 photo Uri 변경 관찰
        viewModel.profilePhotoUri.observe(this, Observer {
            binding.activitySignUpButtonNext.isEnabled = true
            if (it != null) {
                binding.activitySignUpButtonNext.setBackgroundResource(R.drawable.background_round_yellow_24dp)
            }
        })

        //닉네임 Next button 상태 관찰
        viewModel.nickNameNextButtonState.observe(this, Observer {
            binding.activitySignUpButtonNext.isEnabled = it
            if (it) {
                binding.activitySignUpButtonNext.setBackgroundResource(R.drawable.background_round_yellow_24dp)
            } else {
                binding.activitySignUpButtonNext.setBackgroundResource(R.drawable.background_round_gray_24dp)
            }
        })

        //주소 입력했는지 관찰
        viewModel.addressCheck.observe(this, Observer {
            binding.activitySignUpButtonNext.isEnabled = it
            signUpFinishState = it
            if (it) {
                binding.activitySignUpButtonNext.setBackgroundResource(R.drawable.background_round_yellow_24dp)
            } else {
                binding.activitySignUpButtonNext.setBackgroundResource(R.drawable.background_round_gray_24dp)
            }
        })
    }

    fun updateView(position: Int) {
        when (position) {
            0 -> {
                binding.activitySignUpButtonNext.isEnabled = false
                binding.activitySignUpButtonNext.setBackgroundResource(R.drawable.background_round_gray_24dp)
                binding.activitySignUpTextviewTitle.text = "프로필 설정"
                binding.activitySignUpTextviewPosition.text = "회원 가입 1 / 3"
            }
            1 -> {
                binding.activitySignUpButtonNext.isEnabled = false
                binding.activitySignUpButtonNext.setBackgroundResource(R.drawable.background_round_gray_24dp)
                binding.activitySignUpTextviewTitle.text = "닉네임 설정"
                binding.activitySignUpTextviewPosition.text = "회원 가입 2 / 3"
            }
            2 -> {
                binding.activitySignUpButtonNext.isEnabled = false
                binding.activitySignUpButtonNext.setBackgroundResource(R.drawable.background_round_gray_24dp)
                binding.activitySignUpTextviewTitle.text = "주소지 설정"
                binding.activitySignUpTextviewPosition.text = "회원 가입 3 / 3"
                binding.activitySignUpButtonNext.text = "완료"
            }
        }
    }

    fun uploadProfileData() {

        profileViewModel.uploadProfileImage(auth.currentUser!!.uid, Uri.parse(viewModel.profilePhotoUri.value.toString()))

        /*
        var storageRef =
            storage.reference.child("UserProfileImages").child(auth.currentUser!!.uid!!)
        storageRef.putFile(Uri.parse(viewModel.profilePhotoUri.value.toString()))
            .continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
                return@continueWithTask storageRef.downloadUrl
            }.addOnSuccessListener { uri ->
                var map = HashMap<String, Any>()
                map["image"] = uri.toString()
                db.collection("UserProfileImages").document(auth.currentUser!!.uid!!).set(map)
                    .addOnSuccessListener {
                        userDataSave()
                    }
            }

         */
    }

    fun userDataSave() {
        var userData = UserDTO()

        userData.nickName = viewModel.nickName.value.toString()
        userData.timestamp = System.currentTimeMillis()
        userData.totalAddress = viewModel.totalAddress.value.toString()
        userData.zipAddress = viewModel.address.value.toString()
        userData.building = viewModel.buildingAddress.value.toString()
        userData.detailAddress = viewModel.detailAddress.value.toString()
        userData.uid = auth.currentUser!!.uid
        userData.joinAuctionCount = 0

        profileViewModel.uploadUserDataSet(auth.currentUser!!.uid,userData)
        /*
        db.collection("User").document().set(userData)
            .addOnSuccessListener {
                var dataReference = db.collection("nickName").document("nickList")
                db.runTransaction {
                    var dataList = it.get(dataReference).toObject(NickNameDTO::class.java)

                    dataList!!.nickNameList.put(viewModel.nickName.value.toString(),true)
                    println("트랜잭션")
                    it.set(dataReference, dataList)
                    return@runTransaction
                }.addOnSuccessListener {
                    startActivity(Intent(binding.root.context, LobbyActivity::class.java))
                    finish()
                }
            }

         */
    }


}