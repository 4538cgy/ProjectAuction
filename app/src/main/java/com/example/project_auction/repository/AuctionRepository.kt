package com.example.project_auction.repository

import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.data.UserDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

class AuctionRepository {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

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

        /*
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
        }.addOnSuccessListener {
            this@callbackFlow.sendBlocking("TS_PRODUCT_SUCCESS")

            databaseReferenceUser.get().addOnSuccessListener {
                it?.let{
                    if (!it.isEmpty) {
                        it.forEach {
                            if (it["uid"] == uid){


                                db.runTransaction {
                                    transactionUser ->

                                    val transactionReferenceUser = db.collection("User").document(it.id)

                                    this@callbackFlow.sendBlocking("TS_USER_START")

                                    var user = transactionUser.get(transactionReferenceUser).toObject(UserDTO::class.java)

                                    user!!.joinAuction!!.put(productId,true)
                                    user.joinAuctionCount = user.joinAuctionCount!! + 1

                                    //transactionUser.set(transactionReferenceUser,user)


                                    this@callbackFlow.sendBlocking("TS_USER")


                                    return@runTransaction
                                }.addOnSuccessListener {
                                    this@callbackFlow.sendBlocking("TS_USER_SUCCESS")
                                }.addOnFailureListener {
                                    this@callbackFlow.sendBlocking("TS_USER_FAIL : ${it.toString()}")
                                }



                            }
                        }
                    }
                }.run {

                }
            }

        }.addOnFailureListener {
            this@callbackFlow.sendBlocking("TS_PRODUCT_FAIL : ${it.toString()}")
        }
         */


        awaitClose {  }
    }

}