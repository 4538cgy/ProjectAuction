package com.example.project_auction.repository

import android.content.Intent
import android.net.Uri
import com.bumptech.glide.Glide
import com.example.project_auction.data.NickNameDTO
import com.example.project_auction.data.UserDTO
import com.example.project_auction.view.activity.lobby.LobbyActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

class ProfileRepository {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    companion object{
        var profileRepository = ProfileRepository()
    }


    //프로필 이미지 얻어오기
    @ExperimentalCoroutinesApi
    fun getProfileImage(uid : String) = callbackFlow<String> {
       val eventListener = db.collection("UserProfileImages").document(uid).get()
            .addOnCompleteListener {
                it?.let {
                    //none null todo
                    if (it.isSuccessful) {

                        val url = it.result!!["image"].toString()
                        this@callbackFlow.sendBlocking(url)

                    }
                }?.run {
                    //null todo
                }
            }.addOnFailureListener {
            }

        awaitClose { eventListener }
    }

    //닉네임 얻어오기
    @ExperimentalCoroutinesApi
    fun getNickName(uid : String) = callbackFlow<String>{
        val eventListener = db.collection("User").whereEqualTo("uid",uid).get()
            .addOnCompleteListener {
                it?.let{
                    if (it.isSuccessful){
                        val data = it.result.toObjects(UserDTO::class.java)
                        data.forEach {  userDTO ->
                            if (userDTO.uid == uid){
                                this@callbackFlow.sendBlocking(userDTO.nickName!!)
                                return@forEach
                            }

                        }
                    }
                }?.run{

                }
            }

        awaitClose { eventListener }
    }

    //프로필 이미지 저장하기
    @ExperimentalCoroutinesApi
    fun uploadProfileImage(uid : String , profileUri : Uri) = callbackFlow<Boolean> {
        val eventListener = storage.reference.child("UserProfileImages").child(uid)
        eventListener.putFile(profileUri)
            .continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
                return@continueWithTask eventListener.downloadUrl
            }.addOnSuccessListener { uri ->
                var map = HashMap<String, Any>()
                map["image"] = uri.toString()
                db.collection("UserProfileImages").document(uid).set(map)
                    .addOnSuccessListener {
                        this@callbackFlow.sendBlocking(true)
                    }.addOnFailureListener {
                        this@callbackFlow.sendBlocking(false)
                    }
            }

        awaitClose { eventListener }
    }

    //회원 정보 등록
    @ExperimentalCoroutinesApi
    fun uploadUserDataSet(uid : String , userDTO : UserDTO) = callbackFlow<Boolean> {
        val eventListener = db.collection("User").document().set(userDTO)
            .addOnSuccessListener {
                var dataReference = db.collection("nickName").document("nickList")
                db.runTransaction {
                    var dataList = it.get(dataReference).toObject(NickNameDTO::class.java)

                    dataList!!.nickNameList.put(userDTO.nickName.toString(),true)
                    println("트랜잭션")
                    it.set(dataReference, dataList)
                    return@runTransaction
                }.addOnSuccessListener {
                   this@callbackFlow.sendBlocking(true)
                }.addOnFailureListener {
                    this@callbackFlow.sendBlocking(false)
                }
            }

        awaitClose { eventListener }
    }

    @ExperimentalCoroutinesApi
    fun updateNickName(uid : String, nickname : String) = callbackFlow<Boolean> {
        val eventListener = db.collection("User").whereEqualTo("uid",uid).get().addOnSuccessListener {
            it?.let {
                if (!it.isEmpty){
                    it.documents.forEach {
                        if (it["uid"] == uid){
                            var data = it.toObject(UserDTO::class.java)

                            val transactionRoot = db.collection("User").document(it.id)

                            db.runTransaction {
                                transaction ->

                                data!!.nickName = nickname

                                transaction.set(transactionRoot,data)
                                this@callbackFlow.sendBlocking(true)
                                return@runTransaction

                            }
                            return@forEach
                        }
                    }
                }
            }?.run {

            }
        }

        awaitClose { eventListener }
    }
}