package com.example.project_auction.view.activity.detailproduct

import android.content.Intent
import android.icu.util.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
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
import com.example.project_auction.extension.toast
import com.example.project_auction.util.fcm.FcmPush
import com.example.project_auction.util.http.HttpApi
import com.example.project_auction.util.time.TimeUtil
import com.example.project_auction.view.bottomsheet.BottomSheetBidding
import com.example.project_auction.view.bottomsheet.BottomSheetSetCloseProduct
import kotlinx.android.synthetic.main.activity_detail_auction.*
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
import java.text.DecimalFormat

class DetailAuctionActivity : BaseActivity<ActivityDetailAuctionBinding>(R.layout.activity_detail_auction), BottomSheetBidding.BottomSheetButtonClickListener {

    private var data : ProductAuctionDTO ?= null
    lateinit var dataId : String
    lateinit var countDownTimer: CountDownTimer
    var product = ProductAuctionDTO()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activitydetailauction = this

        dataId = intent.getStringExtra("productId").toString()


        getProductData(dataId)
        //경매 참여 옵저빙
        auctionViewModel.joiningState.observe(this, Observer {
            if (it == "TS_USER_SUCCESS") {
                binding.activityDetailAuctionButtonJoin.text = "경매 참여중"
                binding.activityDetailAuctionButtonJoin.isEnabled = false
                binding.activityDetailAuctionButtonBidding.visibility = View.VISIBLE
                getProductData(dataId)
                FcmPush().sendMessage(data!!.uid.toString(),"경매 참여 알람","경매품 : " + data!!.title + "에 신규 참여자가 있습니다.")
            }
        })



        binding.apply {

            activityDetailAuctionConstFrontground.visibility = View.VISIBLE
            //입찰 버튼
            activityDetailAuctionButtonBidding.setOnClickListener {
                val bottomSheetBidding = BottomSheetBidding()

                var bundle = Bundle()
                bundle.putString("currentCost",data!!.currentCost)

                bottomSheetBidding.apply {
                    arguments = bundle
                    show(supportFragmentManager,"lol")
                }

            }


            //뒤로가기
            activityDetailAuctionImagebuttonClose.setOnClickListener { 
                finish()
            }

            //경매 참여 버튼
            activityDetailAuctionButtonJoin.setOnClickListener {
                onAuctionJoin()
            }


        }
    }


    private fun getProductData(dataId : String){
        println("데이터 변경")
        db.collection("productAuction").document(dataId).addSnapshotListener { value, error ->

            if (value != null){
                if (value.exists()){
                    data = value.toObject(ProductAuctionDTO::class.java)!!
                    binding.activityDetailAuctionConstFrontground.visibility = View.GONE
                    updateView()
                    println("데이터 변경완료")
                }
            }
        }
    }




    private fun updateView(){
        binding.activityDetailAuctionTextviewCurrentCost.text = "현재 경매가 : " + DecimalFormat("#,###").format(data!!.currentCost!!.toLong()).toString() + "원"
        binding.activityDetailAuctionTextviewJoiners.text = "현재 참여자 : " + data!!.joinCount.toString() + "명"

        //현재 참여자
        binding.activityDetailAuctionTextviewJoiners.text = "현재 참여자 : " + data!!.joinCount.toString() + "명"
        //상품 제목
        binding.activityDetailAuctionTextviewTitle.text = data!!.title

        //현재 경매가
        binding.activityDetailAuctionTextviewCurrentCost.text = "현재 경매가 : " + data!!.currentCost.toString() + "원"

        //카테고리
        binding.activityDetailAuctionTextviewCategory.text = data!!.category

        //게시시간
        binding.activityDetailAuctionTextviewTime.text = TimeUtil().formatTimeString(data!!.timestamp!!.toLong()).toString()

        //상품 설명
        binding.activityDetailAuctionTextviewExplain.text = data!!.content

        //뷰페이저
        binding.activityDetailAuctionViewpager.adapter = LargeSizePhotoAdapter(binding.root.context,
            data!!.photoList!!
        )
        //인티케이터
        binding.activityDetailAuctionIndicator.setViewPager( binding.activityDetailAuctionViewpager)


        //닉네임
        getUserNickName(data!!.uid.toString())
        //프로필 이미지
        getProfileImage(data!!.uid.toString())
        //경매 종료시간
        setCountDown()

        db.collection("User").whereEqualTo("uid",data!!.bestBidder.toString())
            .addSnapshotListener { value, error ->
                value?.let {
                    //none - null todo
                    if (!it.isEmpty) {
                        val datas = it.toObjects(UserDTO::class.java)
                        datas.forEach { userData ->
                            if (userData.uid.equals(auth.currentUser!!.uid)) {
                                binding.activityDetailAuctionTextviewBestBidder.text = "최고 입찰자 : " + userData.nickName.toString()

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

    private fun onAuctionJoin(){
        auctionViewModel.onJoinAuction(auth.uid!!, dataId)
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
        println("왜 종료되냐고 ")
    }



    private fun setCountDown(){

        binding.activityDetailAuctionTextviewClosetime.text = "데이터 불러오는 중"

        CoroutineScope(Dispatchers.Main).launch {
            getTimestamp(0).collect {
                val time = data!!.closeTimestamp!! - it.data!!.nowTime!!.toLong()
                countDownTimer = object : CountDownTimer(time, 1000){
                    override fun onTick(millisUntilFinished: Long) {
                        binding.activityDetailAuctionTextviewClosetime.text = "경매 종료까지 : " + TimeUtil().formatCloseTimeString(millisUntilFinished/1000).toString()
                        //경매 참여중인지 체크
                        if (data!!.joiners.containsKey(auth.currentUser!!.uid)){
                            binding.activityDetailAuctionButtonJoin.text = "경매 참여중"
                            binding.activityDetailAuctionButtonJoin.isEnabled = false
                            binding.activityDetailAuctionButtonBidding.visibility = View.VISIBLE

                        }
                        binding.activityDetailAuctionConstFrontground.visibility = View.GONE
                    }

                    override fun onFinish() {
                        binding.activityDetailAuctionTextviewClosetime.text = "종료된 경매입니다."
                        binding.activityDetailAuctionButtonJoin.text = "참여 불가"
                        binding.activityDetailAuctionButtonJoin.isEnabled = false
                        binding.activityDetailAuctionButtonBidding.visibility = View.GONE
                        binding.activityDetailAuctionConstFrontground.visibility = View.GONE
                    }

                }.start()
            }
        }



    }

    override fun onBottomSheetButtonClick(text: String) {
        //Current Cost 변경 DB 접근로직
        updateCurrentCost(text)
    }

    fun updateCurrentCost(cost : String){
        val databaseReference = db.collection("productAuction").document(dataId)
        db.runTransaction {
            transaction ->

            product = transaction.get(databaseReference).toObject(ProductAuctionDTO::class.java)!!

            if (product.currentCost!!.toLong() < cost.toLong()){
                product.bestBidder = auth.currentUser!!.uid
            }else{
                runOnUiThread {
                    toast("입찰가 보다 더 큰 금액의 우선 입찰자가 존재합니다.")
                }
                return@runTransaction
            }

            product!!.currentCost = ""+cost
            println("으아아 ${product.currentCost.toString()}")
            transaction.set(databaseReference,product)
            return@runTransaction
        }.addOnCompleteListener {
            println("현재 입찰가 수정완료")
        }.addOnFailureListener {
            println("현재 입찰가 수정실패 사유 : ${it.toString()}")
        }.addOnSuccessListener {
            //입찰 눌렸으면 데이터 수정
            binding.activityDetailAuctionConstFrontground.visibility = View.VISIBLE
            getProductData(dataId)
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



    private fun getUserNickName(uid: String){

        db.collection("User").whereEqualTo("uid",uid)
            .addSnapshotListener { value, error ->
                value?.let {
                    //none - null todo
                    if (!it.isEmpty) {
                        val datas = it.toObjects(UserDTO::class.java)
                        datas.forEach { userData ->
                            if (userData.uid.equals(uid)) {
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

    private fun getProfileImage(uid: String){
        db.collection("UserProfileImages").document(uid).get().addOnCompleteListener {
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