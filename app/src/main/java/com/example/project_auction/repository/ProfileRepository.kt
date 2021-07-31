package com.example.project_auction.repository

import com.bumptech.glide.Glide
import com.example.project_auction.data.UserDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

class ProfileRepository {

    private val db = FirebaseFirestore.getInstance()

    companion object{
        var profileRepository = ProfileRepository()
    }

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
}