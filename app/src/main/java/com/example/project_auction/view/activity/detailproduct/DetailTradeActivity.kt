package com.example.project_auction.view.activity.detailproduct

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.adapter.LargeSizePhotoAdapter
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.data.ProductTradeDTO
import com.example.project_auction.data.UserDTO
import com.example.project_auction.databinding.ActivityDetailAuctionBinding
import com.example.project_auction.databinding.ActivityDetailTradeBinding
import com.example.project_auction.repository.ProductCollectionRepository
import com.example.project_auction.util.time.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class DetailTradeActivity : BaseActivity<ActivityDetailTradeBinding>(R.layout.activity_detail_trade){

    lateinit var data : ProductTradeDTO
    lateinit var dataId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activitydetailtrade = this

        data = intent.getSerializableExtra("tradeProduct") as ProductTradeDTO
        dataId = intent.getStringExtra("tradeProductId").toString()

        println("아아아 ${data.toString()} , 아아앜 ${dataId}")

        binding.apply {
            //뒤로가기
            activityDetailTradeImagebuttonClose.setOnClickListener {
                finish()
            }

            //뷰페이저
            activityDetailTradeViewpager2.adapter = LargeSizePhotoAdapter(binding.root.context,
                data!!.photoList!!
            )
            //인티케이터
            activityDetailTradeIndicator.setViewPager( binding.activityDetailTradeViewpager2)

            //제목
            activityDetailTradeTextviewTitle.text = data.title.toString()

            //카테고리
            activityDetailTradeTextviewCategory.text = data.category.toString()
            //프로필 이미지
            getProfileImage()
            //닉네임
            getUserNickName()
            //가격
            activityDetailTradeTextviewCost.text = "가격 " +DecimalFormat("#,###").format(data.cost?.replace(",","")!!.toLong()).toString() + "원"
            //좋아요 갯수
            activityDetailTradeTextviewFavoriteCount.text = data.favoriteCount.toString()
            //옵션 - 중고여부
            activityDetailTradeTextviewNeworold.text =data.productState.toString()
            //옵션 - 교환 가능 여부
            activityDetailTradeTextviewExchange.text = data.exchangeState.toString()
            //옵션 - 거래 방법
            activityDetailTradeDelivery.text = data.tradeMethod.toString()
            //내용
            activityDetailTradeTextviewExplain.text = data.content.toString()
            //XXX님의 다른 판매글

            //시간
            activityDetailTradeTextviewTimestamp.text = TimeUtil().formatTimeString(data.timestamp!!.toLong())

            //메세지 보내기
            activityDetailTradeButtonSendmessage.setOnClickListener {
                //채팅창 열기
            }
            //찜했는지 체크
            checkFavorite()

            //찜하기
            activityDetailTradeButtonFavorite.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    ProductCollectionRepository().updateFavoriteTrade(dataId,auth.currentUser!!.uid).collect {
                        checkFavorite()
                    }
                }
            }

        }
    }

    private fun checkFavorite(){
        CoroutineScope(Dispatchers.Main).launch {
            ProductCollectionRepository().checkFavoriteTrade(dataId,auth.currentUser!!.uid).collect {
                if (it){
                    //노랑색
                    binding.activityDetailTradeButtonFavorite.apply {
                        setBackgroundResource(R.drawable.background_round_yellow_24dp)
                        text = "찜완료"
                    }

                }else{
                    //회색
                    binding.activityDetailTradeButtonFavorite.apply {
                        setBackgroundResource(R.drawable.background_round_gray_24dp)
                        text = "찜하기"
                    }
                }
            }
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
                        .into(binding.activityDetailTradeImageviewProfile)
                }
            }?.run {
                //null todo
                println("null이야")
            }
        }.addOnFailureListener {
            println("실패 ${it.toString()}")
        }
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
                                binding.activityDetailTradeTextviewNickname.text = userData.nickName.toString()

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
}