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
import com.example.project_auction.util.http.HttpApi
import com.example.project_auction.util.time.TimeUtil
import com.example.project_auction.view.bottomsheet.BottomSheetBidding
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
        //?????? ?????? ?????????

        auctionViewModel.joiningState.observe(this, Observer {
            if (it == "TS_USER_SUCCESS") {
                binding.activityDetailAuctionButtonJoin.text = "?????? ?????????"
                binding.activityDetailAuctionButtonJoin.isEnabled = false
                binding.activityDetailAuctionButtonBidding.visibility = View.VISIBLE
                getProductData(dataId)
                auctionViewModel.onJoinAlarm(data!!.uid.toString(), data!!.title.toString())
            }
        })



        binding.apply {


            activityDetailAuctionConstFrontground.visibility = View.VISIBLE
            //?????? ??????
            activityDetailAuctionButtonBidding.setOnClickListener {
                val bottomSheetBidding = BottomSheetBidding()

                var bundle = Bundle()
                bundle.putString("currentCost",data!!.currentCost)

                bottomSheetBidding.apply {
                    arguments = bundle
                    show(supportFragmentManager,"lol")
                }

            }


            //????????????
            activityDetailAuctionImagebuttonClose.setOnClickListener { 
                finish()
            }

            //?????? ?????? ??????
            activityDetailAuctionButtonJoin.setOnClickListener {
                onAuctionJoin()
            }


        }
    }


    private fun getProductData(dataId : String){
        println("????????? ??????")
        db.collection("productAuction").document(dataId).addSnapshotListener { value, error ->

            if (value != null){
                if (value.exists()){
                    data = value.toObject(ProductAuctionDTO::class.java)!!
                    binding.activityDetailAuctionConstFrontground.visibility = View.GONE
                    updateView()
                    println("????????? ????????????")
                }
            }
        }
    }




    private fun updateView(){
        binding.activityDetailAuctionTextviewCurrentCost.text = "?????? ????????? : " + DecimalFormat("#,###").format(data!!.currentCost!!.toLong()).toString() + "???"
        binding.activityDetailAuctionTextviewJoiners.text = "?????? ????????? : " + data!!.joinCount.toString() + "???"

        //?????? ?????????
        binding.activityDetailAuctionTextviewJoiners.text = "?????? ????????? : " + data!!.joinCount.toString() + "???"
        //?????? ??????
        binding.activityDetailAuctionTextviewTitle.text = data!!.title

        //?????? ?????????
        binding.activityDetailAuctionTextviewCurrentCost.text = "?????? ????????? : " + data!!.currentCost.toString() + "???"

        //????????????
        binding.activityDetailAuctionTextviewCategory.text = data!!.category

        //????????????
        binding.activityDetailAuctionTextviewTime.text = TimeUtil().formatTimeString(data!!.timestamp!!.toLong()).toString()

        //?????? ??????
        binding.activityDetailAuctionTextviewExplain.text = data!!.content

        //????????????
        binding.activityDetailAuctionViewpager.adapter = LargeSizePhotoAdapter(binding.root.context,
            data!!.photoList!!
        )
        //???????????????
        binding.activityDetailAuctionIndicator.setViewPager( binding.activityDetailAuctionViewpager)


        //?????????
        getUserNickName(data!!.uid.toString())
        //????????? ?????????
        getProfileImage(data!!.uid.toString())
        //?????? ????????????
        setCountDown()

        db.collection("User").whereEqualTo("uid",data!!.bestBidder.toString())
            .addSnapshotListener { value, error ->
                value?.let {
                    //none - null todo
                    if (!it.isEmpty) {
                        val datas = it.toObjects(UserDTO::class.java)
                        datas.forEach { userData ->
                            if (userData.uid.equals(auth.currentUser!!.uid)) {
                                binding.activityDetailAuctionTextviewBestBidder.text = "?????? ????????? : " + userData.nickName.toString()

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
        println("??? ??????????????? ")
    }



    private fun setCountDown(){

        binding.activityDetailAuctionTextviewClosetime.text = "????????? ???????????? ???"

        CoroutineScope(Dispatchers.Main).launch {
            getTimestamp(0).collect {
                val time = data!!.closeTimestamp!! - it.data!!.nowTime!!.toLong()
                countDownTimer = object : CountDownTimer(time, 1000){
                    override fun onTick(millisUntilFinished: Long) {
                        binding.activityDetailAuctionTextviewClosetime.text = "?????? ???????????? : " + TimeUtil().formatCloseTimeString(millisUntilFinished/1000).toString()
                        //?????? ??????????????? ??????
                        if (data!!.joiners.containsKey(auth.currentUser!!.uid)){
                            binding.activityDetailAuctionButtonJoin.text = "?????? ?????????"
                            binding.activityDetailAuctionButtonJoin.isEnabled = false
                            binding.activityDetailAuctionButtonBidding.visibility = View.VISIBLE

                        }
                        binding.activityDetailAuctionConstFrontground.visibility = View.GONE
                    }

                    override fun onFinish() {
                        binding.activityDetailAuctionTextviewClosetime.text = "????????? ???????????????."
                        binding.activityDetailAuctionButtonJoin.text = "?????? ??????"
                        binding.activityDetailAuctionButtonJoin.isEnabled = false
                        binding.activityDetailAuctionButtonBidding.visibility = View.GONE
                        binding.activityDetailAuctionConstFrontground.visibility = View.GONE
                    }

                }.start()

                if (data!!.uid == auth.currentUser!!.uid){
                    binding.activityDetailAuctionButtonBidding.isEnabled = false
                    binding.activityDetailAuctionButtonBidding.text = "?????? ??????"
                    binding.activityDetailAuctionTextviewClosetime.text = "?????? ????????? ?????????."
                    
                }
            }
        }



    }

    override fun onBottomSheetButtonClick(text: String) {
        //Current Cost ?????? DB ????????????
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
                    toast("????????? ?????? ??? ??? ????????? ?????? ???????????? ???????????????.")
                }
                return@runTransaction
            }

            product!!.currentCost = ""+cost
            println("????????? ${product.currentCost.toString()}")
            transaction.set(databaseReference,product)
            return@runTransaction
        }.addOnCompleteListener {
            println("?????? ????????? ????????????")
        }.addOnFailureListener {
            println("?????? ????????? ???????????? ?????? : ${it.toString()}")
        }.addOnSuccessListener {
            //?????? ???????????? ????????? ??????
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
                println("??????! ${call.toString()} ll ${t.toString()}")
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
                    println("?????? ????????? ${url.toString()}")
                    Glide.with(binding.root.context)
                        .load(url)
                        .circleCrop()
                        .thumbnail(0.1f)
                        .into(binding.activityDetailAuctionImageviewProfile)
                }
            }?.run {
                //null todo
                println("null??????")
            }
        }.addOnFailureListener {
            println("?????? ${it.toString()}")
        }
    }
}