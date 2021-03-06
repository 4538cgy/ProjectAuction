package com.example.project_auction.view.activity.addpost

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_auction.R
import com.example.project_auction.adapter.PhotoAdapter
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.data.ProductTradeDTO
import com.example.project_auction.data.UserDTO
import com.example.project_auction.databinding.ActivityAddAuctionPostBinding
import com.example.project_auction.databinding.ActivityAddTradePostBinding
import com.example.project_auction.extension.toast
import com.example.project_auction.util.time.TimeUtil
import com.example.project_auction.view.bottomsheet.BottomSheetCategory
import com.example.project_auction.view.bottomsheet.BottomSheetPostOption
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.DecimalFormat

class AddTradePostActivity :
    BaseActivity<ActivityAddTradePostBinding>(R.layout.activity_add_trade_post),BottomSheetPostOption.BottomSheetSetCloseProductButtonClickListener, BottomSheetCategory.BottomSheetButtonClickListener {


    private var photoList = arrayListOf<String>()
    private var photoUploadCount = 0
    private var photoDownloadUrlList = arrayListOf<String>()
    private var thousandFormatCostResult = ""
    private var thousandFormat = DecimalFormat("###,###")
    //중고여부
    private var newState = ""
    //직거래 여부
    private var meetState = ""
    //교환 여부
    private var transCheck = ""

    private val addPhotoCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if (it.resultCode == RESULT_OK) {
                if (it.data!!.clipData != null) {

                    println("데이터 있음")

                    val count = it.data!!.clipData!!.itemCount
                    var currentItem = 0
                    while (currentItem < count) {
                        val imageUri =
                            it.data!!.clipData!!.getItemAt(currentItem).uri
                        photoList.add(imageUri.toString())
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        currentItem += 1
                    }
                } else {

                    println("데이터 있음 단일")
                    val fullPhotoUri: Uri = it.data!!.data!!
                    photoList.add(fullPhotoUri.toString())
                }
                binding.activityAddTradePostRecycler.adapter!!.notifyDataSetChanged()
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activityaddtradepost = this
        binding.apply {

            //뒤로가기
            activityAddTradePostImagebuttonClose.setOnClickListener {
                finish()
            }
            //사진 등록 + 어댑터
            activityAddTradePostRecycler.adapter =
                PhotoAdapter(binding.root.context, photoList, true)
            activityAddTradePostRecycler.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)

            activityAddTradePostImagebuttonPhoto.setOnClickListener {
                //사진 추가
                addPhoto()
                (activityAddTradePostRecycler.adapter as PhotoAdapter).notifyDataSetChanged()
            }

            //카테고리
            activityAddTradePostTextviewCategory.setOnClickListener {
                //카테고리 바텀 시트 오픈
                val categorySheet = BottomSheetCategory()
                categorySheet.show(supportFragmentManager,"lol")
            }

            //주소
            getUserAddress()

            //상품명
            activityAddTradePostEdittextTitle.addTextChangedListener {
                activityAddTradePostTextviewTitleCount.text = it!!.length.toString() + "/24"
                if (it!!.length < 23){
                    activityAddTradePostTextviewTitleCount.setTextColor(Color.GRAY)
                }else{
                    activityAddTradePostTextviewTitleCount.setTextColor(Color.RED)
                }
            }

            //상품 설명
            activityAddTradePostEdittextExplain.addTextChangedListener {
                activityAddTradePostTextviewExplainCount.text = it!!.length.toString() + "/800"
                if (it.length < 760){
                    activityAddTradePostTextviewExplainCount.setTextColor(Color.GRAY)
                }else{
                    activityAddTradePostTextviewExplainCount.setTextColor(Color.RED)
                }
            }
            
            //옵션
            activityAddTradePostTextviewOption.setOnClickListener { 
                //바텀 시트 오픈
                val bottomSheet = BottomSheetPostOption()
                bottomSheet.show(supportFragmentManager, "lol")
            }
            
            //상품 등록
            activityAddTradePostButtonUpload.setOnClickListener {

                if (photoList.isEmpty()){
                    toast("사진을 1장 이상 추가해주세요.")
                }else if (activityAddTradePostEdittextTitle.text.isEmpty()){
                    toast("상품명을 기입해주세요.")
                }else if (activityAddTradePostTextviewCategory.text == "카테고리")
                {
                    toast("카테고리를 입력해주세요.")
                }else if (activityAddTradePostEdittextCost.text.isEmpty()){
                    toast("상품 가격을 입력해주세요.")
                }else if (activityAddTradePostEdittextExplain.text.isEmpty()){
                    toast("상품 설명을 입력해주세요.")
                }else if (meetState == null || newState == null || transCheck == null){
                    toast("세부 사항을 입력해주세요.")
                }else{
                    activityAddTradePostConstFront.visibility = View.VISIBLE
                    activityAddTradePostButtonUpload.visibility = View.GONE
                    uploadContent()
                }

            }

            //상품 가격 , 처리
            val textWatcherCost = object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(thousandFormatCostResult)) {
                        thousandFormatCostResult = thousandFormat.format(s.toString().replace(",".toRegex(), "").toLong())
                        activityAddTradePostEdittextCost.setText(
                            thousandFormatCostResult
                        )
                        activityAddTradePostEdittextCost.setSelection(
                            thousandFormatCostResult.length
                        )
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
            activityAddTradePostEdittextCost.addTextChangedListener(textWatcherCost)

        }
    }

    //상품 등록 로직
    fun uploadContent(){

        if (photoUploadCount < photoList.size && photoList.size != 0) {
            uploadPhoto(photoList[photoUploadCount])
        } else {
            uploadProudct()
        }

    }

    //사진 등록
    fun uploadPhoto(uri : String){
        var timestamp = TimeUtil().getTime()
        var imageFileName = "Trade_Product_IMAGE_" + timestamp + "_.png"

        var storageRef =
            FirebaseStorage.getInstance().reference.child("product_trade").child(imageFileName)

        storageRef.putFile(Uri.parse(uri))
            ?.continueWithTask { task: com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> ->

                return@continueWithTask storageRef.downloadUrl
            }?.addOnSuccessListener { uri ->


                photoUploadCount++
                photoDownloadUrlList.add(uri.toString())

                uploadProudct()

            }
    }


    //상품 등록
    fun uploadProudct(){
        var productTradeData = ProductTradeDTO()
        productTradeData.uid = auth.currentUser!!.uid
        productTradeData.photoList = photoDownloadUrlList
        productTradeData.title = binding.activityAddTradePostEdittextTitle.text.toString()
        productTradeData.timestamp = System.currentTimeMillis()
        productTradeData.cost = binding.activityAddTradePostEdittextCost.text.toString()
        productTradeData.content = binding.activityAddTradePostEdittextExplain.text.toString()
        productTradeData.category = binding.activityAddTradePostTextviewCategory.text.toString()
        productTradeData.delete = false
        productTradeData.viewCount = 0
        productTradeData.favoriteCount = 0
        productTradeData.tradeMethod = meetState
        productTradeData.productState = newState
        productTradeData.exchangeState = transCheck
        productTradeData.tradeState = 0
        
        db.collection("ProductTrade").document().set(productTradeData).addOnCompleteListener { 
            println("성공")
            finish()
        }.addOnFailureListener { 
            println("실패")
        }
    }


    //주소 얻어오기기
    fun getUserAddress(){
       db.collection("User").whereEqualTo("uid",auth.currentUser!!.uid).get()
            .addOnCompleteListener {
                it?.let {
                    if (it.isSuccessful){
                        var data = it.result.toObjects(UserDTO::class.java)
                        data.forEach {
                            if (it.uid == auth.currentUser!!.uid){
                                binding.activityAddTradePostTextviewAddress.text = it.zipAddress.toString()
                            }
                        }
                    }
                }.run {

                }
            }
    }

    fun addPhoto() {

        toast("사진을 꾹 누르시면 여러장을 선택할 수 있습니다.")

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }

        addPhotoCallback.launch(intent)
    }

    override fun onBottomSheetSetCloseProductButtonClick(
        tradeOption: String,
        productState: String,
        transState: String
    ) {

        newState = productState
        meetState = tradeOption
        transCheck = transState

        binding.activityAddTradePostTextviewOption.text = "세부 사항 : $tradeOption / $productState / $transState"
    }

    override fun onBottomSheetButtonClick(text: String) {
        binding.activityAddTradePostTextviewCategory.text = text.toString()
    }
}