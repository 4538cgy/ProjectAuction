package com.example.project_auction.view.activity.detailproduct

import android.icu.util.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.adapter.LargeSizePhotoAdapter
import com.example.project_auction.adapter.PhotoAdapter
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.data.TimeRequestDTO
import com.example.project_auction.data.UserDTO
import com.example.project_auction.databinding.ActivityDetailAuctionBinding
import com.example.project_auction.util.http.HttpApi
import com.example.project_auction.util.time.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailAuctionActivity : BaseActivity<ActivityDetailAuctionBinding>(R.layout.activity_detail_auction) {

    lateinit var data : ProductAuctionDTO
    lateinit var countDownTimer: CountDownTimer

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
            getUserNickName()
            //프로필 이미지
            getProfileImage()
            //경매 종료시간
            setCountDown()
            //경매 참여 버튼
            activityDetailAuctionButtonJoin.setOnClickListener {

            }
            //현재 참여자
            activityDetailAuctionTextviewJoiners.text = "현재 참여자 : " + data.joinCount.toString() + "명"

            //옵션 버튼튼
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

    private fun setCountDown(){

        CoroutineScope(Dispatchers.Main).launch {
            getTimestamp(0).collect {
                val time = data.closeTimestamp!! - it.data!!.nowTime!!.toLong()
                countDownTimer = object : CountDownTimer(time, 1000){
                    override fun onTick(millisUntilFinished: Long) {
                        binding.activityDetailAuctionTextviewClosetime.text = "경매 종료까지 : " + (millisUntilFinished/1000).toString()
                    }

                    override fun onFinish() {
                        println("경매가 종료되었습니다.")
                    }

                }.start()
            }
        }



    }

    @ExperimentalCoroutinesApi
    fun getTimestamp(days : Int) = callbackFlow<TimeRequestDTO.TimeResponse>{
        var data = TimeRequestDTO.Time()
        data.day = days

        HttpApi().test3(data).enqueue(object : Callback<TimeRequestDTO.TimeResponse> {
            override fun onResponse(
                call: Call<TimeRequestDTO.TimeResponse>,
                response: Response<TimeRequestDTO.TimeResponse>
            ) {
                println("${response.message()}")
                println("${response.code()}")
                println("${response.body()}")

                this@callbackFlow.sendBlocking(response.body()!!)
            }

            override fun onFailure(call: Call<TimeRequestDTO.TimeResponse>, t: Throwable) {
                println("실패! ${call.toString()} ll ${t.toString()}")
            }


        })

        awaitClose {  }
    }



    private fun getUserNickName(){
        db.collection("User").whereEqualTo("uid",auth.currentUser!!.uid)
            .addSnapshotListener { value, error ->
                value?.let {
                    //none - null todo
                    if (!it.isEmpty) {
                        val datas = it.toObjects(UserDTO::class.java)
                        datas.forEach { userData ->
                            if (userData.uid.equals(auth.currentUser!!.uid)) {
                                binding.activityDetailAuctionTextviewNickname.text = userData.nickName.toString()

                                return@addSnapshotListener
                            }
                        }
                    }
                }?.run {
                    //null todo
                }

                return@addSnapshotListener
            }
    }

    private fun getProfileImage(){
        db.collection("UserProfileImages").document(auth.currentUser!!.uid).get().addOnCompleteListener {
            it?.let {
                //none null todo
                if (it.isSuccessful){

                    val url = it.result!!["image"].toString()
                    println("uid ${auth.currentUser!!.uid}")
                    println("사진 데이터 ${url.toString()}")
                    Glide.with(binding.root.context)
                        .load(url)
                        .circleCrop()
                        .thumbnail(0.1f)
                        .into(binding.activityDetailAuctionImageviewProfile)
                }
            }?.run {
                //null todo
                println("null이야")
            }
        }.addOnFailureListener {
            println("실패 ${it.toString()}")
        }
    }
}