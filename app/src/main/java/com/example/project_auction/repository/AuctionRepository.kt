package com.example.project_auction.repository

import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.data.ProductTradeDTO
import com.example.project_auction.data.UserDTO
import com.example.project_auction.util.http.HttpApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuctionRepository {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    companion object{
        val auctionRepository = AuctionRepository()
    }

    //경매글 가져오기 map(document key,document value)
    @ExperimentalCoroutinesApi
    fun loadAuctionData(page : Int, orderBy : Int, uid : String , sortKey : String ) = callbackFlow<ProductAuctionDTO.ProductResponseDTO?> {
        val eventListener = HttpApi().getAuctionProduct(page,orderBy,uid,sortKey).enqueue(object : Callback<ProductAuctionDTO.ProductResponseDTO>{
            override fun onResponse(
                call: Call<ProductAuctionDTO.ProductResponseDTO>,
                response: Response<ProductAuctionDTO.ProductResponseDTO>
            ) {

                if (response.body() != null) {
                    this@callbackFlow.sendBlocking(response.body()!!)
                }else{
                    this@callbackFlow.sendBlocking(null)
                }
            }

            override fun onFailure(call: Call<ProductAuctionDTO.ProductResponseDTO>, t: Throwable) {

            }

        })

        awaitClose { eventListener }
    }

    //거래글 가져오기 map(document key, document value
    @ExperimentalCoroutinesApi
    fun loadTradeData(page: Int , orderBy: Int , uid: String , sortKey: String , endFlag : Boolean) = callbackFlow<ProductTradeDTO.ProductResponseDTO?>{

        val eventListener = HttpApi().getTradeProduct(page , orderBy , uid , sortKey, endFlag).enqueue(object : Callback<ProductTradeDTO.ProductResponseDTO>{
            override fun onResponse(
                call: Call<ProductTradeDTO.ProductResponseDTO>,
                response: Response<ProductTradeDTO.ProductResponseDTO>
            ) {
                if (response.body() != null) {
                    this@callbackFlow.sendBlocking(response.body()!!)
                }else{
                    this@callbackFlow.sendBlocking(null)
                }
            }

            override fun onFailure(call: Call<ProductTradeDTO.ProductResponseDTO>, t: Throwable) {

            }

        })


        awaitClose { eventListener }

    }

    //경매 참여
    @ExperimentalCoroutinesApi
    fun joinAuction(uid : String, productId : String) = callbackFlow<String> {
        val transactionReferenceProduct = db.collection("productAuction").document(productId)
        val databaseReferenceUser = db.collection("User").whereEqualTo("uid",uid)
        databaseReferenceUser.get().addOnCompleteListener {
            if (it != null){
                if (it.isSuccessful){
                    it.result.forEach {
                        if (it["uid"] == uid){
                            db.runTransaction {
                                    transactionProduct ->
                                //경매 글에 참여자 uid 올리기
                                this@callbackFlow.sendBlocking("TS_PRODUCT_START")
                                var product = transactionProduct.get(transactionReferenceProduct).toObject(ProductAuctionDTO::class.java)

                                product!!.joinCount = product!!.joinCount + 1
                                product.joiners.put(uid,true)
                                transactionProduct.set(transactionReferenceProduct,product)
                                this@callbackFlow.sendBlocking("TS_PRODUCT")
                                return@runTransaction
                            }
                            db.runTransaction {
                                    transactionUser ->

                                val transactionReferenceUser = db.collection("User").document(it.id)

                                this@callbackFlow.sendBlocking("TS_USER_START")

                                var user = transactionUser.get(transactionReferenceUser).toObject(UserDTO::class.java)

                                user!!.joinAuction!!.put(productId,true)
                                user.joinAuctionCount = user.joinAuctionCount!! + 1

                                transactionUser.set(transactionReferenceUser,user)

                                this@callbackFlow.sendBlocking("TS_USER")

                                return@runTransaction
                            }.addOnSuccessListener {
                                this@callbackFlow.sendBlocking("TS_USER_SUCCESS")
                                println("aaaaaaaaaaaa")
                            }.addOnFailureListener {
                                this@callbackFlow.sendBlocking("TS_USER_FAIL : ${it.toString()}")
                                println("bbbbbbbbbbbb")
                            }.addOnCanceledListener {
                                println("cccccccccccc")
                            }
                        }
                    }
                }
            }
        }
        awaitClose {  }
    }

}