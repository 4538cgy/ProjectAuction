package com.example.project_auction.view.activity.addpost

import android.content.Intent
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
import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.databinding.ActivityAddAuctionPostBinding
import com.example.project_auction.extension.toast
import com.example.project_auction.util.time.TimeUtil
import com.example.project_auction.view.bottomsheet.BottomSheetCategory
import com.example.project_auction.view.bottomsheet.BottomSheetSetCloseProduct
import com.google.android.gms.auth.api.Auth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.DecimalFormat

class AddAuctionPostActivity : BaseActivity<ActivityAddAuctionPostBinding>(R.layout.activity_add_auction_post),BottomSheetCategory.BottomSheetButtonClickListener , BottomSheetSetCloseProduct.BottomSheetSetCloseProductButtonClickListener{

    private var photoList = arrayListOf<String>()
    private var photoUploadCount = 0
    private var photoDownloadUrlList = arrayListOf<String>()
    private var thousandFormat = DecimalFormat("###,###")
    private var thousandFormatStartCostResult = ""
    private var thousandFormatCloseCostResult = ""
    private lateinit var textWatcherCloseCost: TextWatcher
    private lateinit var textWatcherStartCost: TextWatcher

    private val addPhotoCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        if (it.resultCode == RESULT_OK){
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
            binding.activityAddAuctionPostRecyclerviewPhotolist.adapter!!.notifyDataSetChanged()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activityaddauctionpost = this
        binding.addproductviewmodel = addProductViewModel


        binding.apply {

            activityAddAuctionPostRecyclerviewPhotolist.adapter = PhotoAdapter(binding.root.context,photoList)
            activityAddAuctionPostRecyclerviewPhotolist.layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.HORIZONTAL,false)


            activityAddAuctionPostImagebuttonClose.setOnClickListener {
                finish()
            }
            
            //상품명
            activityAddAuctionPostEdittextTitle.addTextChangedListener {
                activityAddAuctionPostTextviewTitle.text = it!!.length.toString() + "/24"
            }

            //상품 설명
            activityAddAuctionPostEdittextProductIntro.addTextChangedListener {
                activityAddAuctionPostTextviewProductIntroCount.text = it!!.length.toString() + "/400"
            }

            //시작가
             textWatcherStartCost = object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(thousandFormatStartCostResult)){
                        thousandFormatStartCostResult = thousandFormat.format(s.toString().replace(",".toRegex(),"").toLong())
                        activityAddAuctionPostEdittextStartCost.removeTextChangedListener(textWatcherStartCost)
                        activityAddAuctionPostEdittextStartCost.setText(thousandFormatStartCostResult)
                        activityAddAuctionPostEdittextStartCost.setSelection(thousandFormatStartCostResult.length)
                        activityAddAuctionPostEdittextStartCost.addTextChangedListener(textWatcherStartCost)

                    }
                }

                override fun afterTextChanged(s: Editable?) {}

            }


            //즉시 입찰가
            textWatcherCloseCost = object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(thousandFormatCloseCostResult)){
                        thousandFormatCloseCostResult = thousandFormat.format(s.toString().replace(",".toRegex(),"").toLong())
                        activityAddAuctionPostEdittextCloseCost.removeTextChangedListener(textWatcherCloseCost)
                        activityAddAuctionPostEdittextCloseCost.setText(thousandFormatCloseCostResult)
                        activityAddAuctionPostEdittextCloseCost.setSelection(thousandFormatCloseCostResult.length)
                        activityAddAuctionPostEdittextCloseCost.addTextChangedListener(textWatcherCloseCost)

                    }
                }

                override fun afterTextChanged(s: Editable?) {}

            }
            activityAddAuctionPostEdittextCloseCost.addTextChangedListener(textWatcherCloseCost)
            activityAddAuctionPostEdittextStartCost.addTextChangedListener(textWatcherStartCost)



            //사진 추가
            activityAddAuctionPostButtonPhotoAdd.setOnClickListener {
                //사진 추가
                addPhoto()
                (activityAddAuctionPostRecyclerviewPhotolist.adapter as PhotoAdapter).notifyDataSetChanged()
            }

            activityAddAuctionPostTextviewCategory.setOnClickListener {
                //카테고리 설정
                setCategory()
            }

            //경매 기간 설정
            activityAddAuctionPostTextviewCloseTime.setOnClickListener {
                val bottomSheetSetCloseProduct = BottomSheetSetCloseProduct()
                bottomSheetSetCloseProduct.show(supportFragmentManager,"lol")
            }

            binding.activityAddAuctionPostButtonUpload.setOnClickListener { 
                println("게시글 업로드")
                binding.activityAddAuctionPostConstAllBar.visibility = View.GONE
                binding.activityAddAuctionPostConstTopBar.visibility = View.GONE
                binding.activityAddAuctionPostProgressbar.visibility = View.VISIBLE
                binding.activityAddAuctionPostTextviewLoading.visibility = View.VISIBLE
                binding.activityAddAuctionPostTextviewLoading.text = "사진을 업로드 중입니다. \n앱을 절대 종료하지마세요."
                
                //게시글 완료 체크
                if (photoList.size == 0) {
                    toast("사진을 추가해주세요.")
                }else if (binding.activityAddAuctionPostTextviewCategory.text.toString() == "카테고리 선택"){
                    toast("카테고리를 선택해주세요")
                }else if (binding.activityAddAuctionPostEdittextTitle.text.isEmpty()){
                    toast("상품명을 입력해주세요.")
                }else if (binding.activityAddAuctionPostTextviewCloseTime.text.toString() == "경매 기간 설정"){
                    toast("경매 기간을 입력해주세요.")
                }else if (binding.activityAddAuctionPostEdittextStartCost.text.isEmpty()){
                    toast("경매 시작가를 입력해주세요.")
                }else if (binding.activityAddAuctionPostEdittextProductIntro.text.isEmpty()){
                    toast("상품 설명을 입력해주세요.")
                }else{
                    contentUpload()
                }

            }
        }
    }



    fun contentUpload(){
        if (photoUploadCount < photoList.size && photoList.size != 0){
            uploadPhoto(photoList[photoUploadCount])
        }else{
            uploadProudct()
        }
    }

    fun setCategory(){
        val bottomSheetCategory = BottomSheetCategory()
        bottomSheetCategory.show(supportFragmentManager,"lol")
    }

    fun uploadPhoto(uri : String){
        var timestamp = TimeUtil().getTime()
        var imageFileName = "Auction_Product_IMAGE_" + timestamp + "_.png"

        var storageRef = FirebaseStorage.getInstance().reference.child("product").child(imageFileName)

        storageRef.putFile(Uri.parse(uri))?.continueWithTask { task: com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> ->

            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->


            photoUploadCount ++
            photoDownloadUrlList.add(uri.toString())

            contentUpload()

        }

    }

    fun uploadProudct(){
        binding.activityAddAuctionPostTextviewLoading.text = "게시글을 업로드 중입니다. \n앱을 절대 종료하지마세요."
        var product = ProductAuctionDTO()
        product.title = binding.activityAddAuctionPostEdittextTitle.text.toString()
        product.category = binding.activityAddAuctionPostTextviewCategory.text.toString()
        product.uid = auth.currentUser!!.uid
        if (binding.activityAddAuctionPostEdittextCloseCost.text.isNotEmpty()) {
            product.closeCost = binding.activityAddAuctionPostEdittextCloseCost.text.toString()
        }else{
            product.closeCost = "0"
        }
        product.content = binding.activityAddAuctionPostEdittextProductIntro.text.toString()
        product.delete = false
        product.startCost = binding.activityAddAuctionPostEdittextStartCost.text.toString()
        product.favoriteCount = 0
        product.timestamp = System.currentTimeMillis()
        product.viewCount = 0
        product.photoList = photoDownloadUrlList

        db.collection("productAuction").document().set(product).addOnSuccessListener {
            //success
            finish()
        }.addOnFailureListener {
            //fail
        }
    }


    fun addPhoto(){

        toast("사진을 꾹 누르시면 여러장을 선택할 수 있습니다.")

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        }

        addPhotoCallback.launch(intent)
    }

    override fun onBottomSheetButtonClick(text: String) {
        binding.activityAddAuctionPostTextviewCategory.text = text
    }

    override fun onBottomSheetSetCloseProductButtonClick(text: String) {
        binding.activityAddAuctionPostTextviewCloseTime.text = text + "일"
    }
}