package com.example.project_auction.view.activity.detailproduct

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.adapter.LargeSizePhotoAdapter
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.data.ProductTradeDTO
import com.example.project_auction.data.UserDTO
import com.example.project_auction.databinding.ActivityDetailTradeBinding
import com.example.project_auction.repository.ProductCollectionRepository
import com.example.project_auction.util.time.TimeUtil
import com.example.project_auction.view.activity.chat.TradeChatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class DetailTradeActivity : BaseActivity<ActivityDetailTradeBinding>(R.layout.activity_detail_trade){

    lateinit var productData : ProductTradeDTO
    lateinit var productDataId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activitydetailtrade = this

        productData = intent.getSerializableExtra("tradeProduct") as ProductTradeDTO
        productDataId = intent.getStringExtra("tradeProductId").toString()

        println("아아아 ${productData.toString()} , 아아앜 ${productDataId}")

        binding.apply {
            //뒤로가기
            activityDetailTradeImagebuttonClose.setOnClickListener {
                finish()
            }

            //뷰페이저
            activityDetailTradeViewpager2.adapter = LargeSizePhotoAdapter(binding.root.context,
                productData!!.photoList!!
            )
            //인티케이터
            activityDetailTradeIndicator.setViewPager( binding.activityDetailTradeViewpager2)

            //제목
            activityDetailTradeTextviewTitle.text = productData.title.toString()

            //카테고리
            activityDetailTradeTextviewCategory.text = productData.category.toString()
            //프로필 이미지
            getProfileImage(productData.uid!!)
            //닉네임
            getUserNickName(productData.uid!!)
            //가격
            activityDetailTradeTextviewCost.text = "가격 " +DecimalFormat("#,###").format(productData.cost?.replace(",","")!!.toLong()).toString() + "원"
            //좋아요 갯수
            activityDetailTradeTextviewFavoriteCount.text = productData.favoriteCount.toString()
            //옵션 - 중고여부
            activityDetailTradeTextviewNeworold.text =productData.productState.toString()
            //옵션 - 교환 가능 여부
            activityDetailTradeTextviewExchange.text = productData.exchangeState.toString()
            //옵션 - 거래 방법
            activityDetailTradeDelivery.text = productData.tradeMethod.toString()
            //내용
            activityDetailTradeTextviewExplain.text = productData.content.toString()
            //XXX님의 다른 판매글

            //시간
            activityDetailTradeTextviewTimestamp.text = TimeUtil().formatTimeString(productData.timestamp!!.toLong())

            //메세지 보내기
            activityDetailTradeButtonSendmessage.setOnClickListener {
                //채팅창 열기

                var intent = Intent(binding.root.context,TradeChatActivity::class.java)
                intent.apply {
                    putExtra("productData",productData)
                    putExtra("productDataId",productDataId)
                    putExtra("destinationUid",productData.uid)
                    startActivity(intent)
                }

            }
            //찜했는지 체크
            checkFavorite()

            //찜하기
            activityDetailTradeButtonFavorite.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    ProductCollectionRepository().updateFavoriteTrade(productDataId,auth.currentUser!!.uid).collect {
                        checkFavorite()
                    }
                }
            }

        }
    }

    private fun checkFavorite(){
        CoroutineScope(Dispatchers.Main).launch {
            ProductCollectionRepository().checkFavoriteTrade(productDataId,auth.currentUser!!.uid).collect {
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

    private fun getProfileImage(uid : String){
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

    private fun getUserNickName(uid: String){

        db.collection("User").whereEqualTo("uid",uid)
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