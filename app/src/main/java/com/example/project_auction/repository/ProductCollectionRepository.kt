package com.example.project_auction.repository

import com.example.project_auction.data.ProductAuctionDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

class ProductCollectionRepository {

    private val db = FirebaseFirestore.getInstance()

    @ExperimentalCoroutinesApi
    fun updateFavorite(postId : String,uid : String) = callbackFlow<Boolean> {

        val transactionReference = db.collection("productAuction").document(postId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(transactionReference).toObject(ProductAuctionDTO::class.java)

            if (!snapshot!!.viewers.containsKey(uid))
            {
                //뷰어 추가 + 카운트 1추가
                snapshot.viewCount += 1
                snapshot.viewers.put(uid,true)
                transaction.set(transactionReference,snapshot)
                this@callbackFlow.sendBlocking(true)
                return@runTransaction
            }else{
                //뷰어 삭제 + 카운트 1차감
                snapshot.viewCount -= 1
                snapshot.viewers.remove(uid)
                transaction.set(transactionReference,snapshot)
                this@callbackFlow.sendBlocking(true)
                return@runTransaction
            }
        }.addOnFailureListener {
            this@callbackFlow.sendBlocking(false)
        }

        awaitClose {  }
    }

    @ExperimentalCoroutinesApi
    fun checkFavorite(postId: String, uid : String) = callbackFlow<Boolean> {
        val databaseReference = db.collection("productAuction").document(postId)

        databaseReference.addSnapshotListener { value, error ->
            value?.let{
              //non - null todo
                if (it.exists()){
                    val data = it.toObject(ProductAuctionDTO::class.java)

                    if (data!!.viewers.containsKey(uid)){
                        //이미 좋아요 누름
                        this@callbackFlow.sendBlocking(true)
                    }else{
                        //좋아요 누르지 않음
                        this@callbackFlow.sendBlocking(false)
                    }
                }
            }?.run {
                // null todo
            }
        }

        awaitClose {  }
    }
}