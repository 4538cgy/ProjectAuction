package com.example.project_auction.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.project_auction.R
import com.example.project_auction.data.ChatDTO
import com.example.project_auction.data.UserDTO
import com.example.project_auction.databinding.ItemChatBubbleBinding
import com.example.project_auction.util.time.TimeUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.ArrayList

class ChatAdapter(private val context : Context,private val chatList : ArrayList<ChatDTO.ChatData>,private val destinationUid : String, private val uid: String) : RecyclerView.Adapter<MessageViewHolder>() {

    private val auth = FirebaseAuth.getInstance()
    var userNickName : String ? = null
    private val db = FirebaseFirestore.getInstance()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        //var view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_bubble,parent,false)
        val binding = ItemChatBubbleBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false)
        getUserName()

        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.onBind(chatList[position])

        //내가 보낸 메세지
        if (chatList[position].uid.equals(auth.currentUser!!.uid)) {
            holder.binding.itemChatBubbleTextViewMessage.text = chatList[position].message
            holder.binding.itemChatBubbleTextViewMessage.setBackgroundResource(R.drawable.background_round_gray_stroke_black_4dp)
            holder.binding.itemChatBubbleLinearlayoutDestination.visibility = View.INVISIBLE
            holder.binding.itemChatBubbleTextViewMessage.textSize = 18F

            holder.binding.itemChatBubbleLinearlayoutMain.gravity = Gravity.RIGHT

            //상대방이 보낸 메세지
        } else {
            /*
            Glide.with(holder.itemView.context)
                .load(userModel.profileImageUrl)
                .apply(RequestOptions().circleCrop())
                .into(holder.binding.messageItemImageviewProfile)

             */

            FirebaseFirestore.getInstance()?.collection("profileImages")?.document(chatList[position].uid.toString())?.get()?.addOnCompleteListener {
                    task ->
                if (task.isSuccessful)
                {
                    var url = task.result!!["image"]
                    Glide.with(holder.binding.root.context).load(url).apply(RequestOptions().circleCrop()).into(holder.binding.itemChatBubbleImageviewProfile)

                }
            }

            //유저 닉네임 가져오기
            if (userNickName == null){
                getUserName()
            }else{
                holder.binding.itemChatBubbleTextviewName.text = userNickName
            }


            holder.binding.itemChatBubbleLinearlayoutDestination.visibility = View.VISIBLE
            holder.binding.itemChatBubbleTextViewMessage.setBackgroundResource(R.drawable.background_round_white_stroke_black_2dp)
            holder.binding.itemChatBubbleTextViewMessage.text = chatList[position].message
            holder.binding.itemChatBubbleTextViewMessage.textSize = 18F
            holder.binding.itemChatBubbleLinearlayoutMain.gravity = Gravity.LEFT
        }

        //holder.binding.itemChatBubbleTextviewTimestamp.text = TimeUtil().formatTimeString(chatList[position].timestamp!!.toLong()).toString()
        holder.binding.itemChatBubbleTextviewTimestamp.text = SimpleDateFormat("a HH:mm").format(chatList[position].timestamp!!.toLong())
    }

    override fun getItemCount(): Int {
        if (chatList.size > 0){
            return chatList.size
        }else{
            return 0
        }
    }

    fun getUserName(){
        db.collection("User").whereEqualTo("uid",destinationUid)
            .addSnapshotListener { value, error ->
                value?.let {
                    //none - null todo
                    if (!it.isEmpty) {
                        val datas = it.toObjects(UserDTO::class.java)
                        datas.forEach { userData ->
                            if (userData.uid.equals(auth.currentUser!!.uid)) {
                                userNickName = userData.nickName.toString()

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



class MessageViewHolder(var binding: ItemChatBubbleBinding) : RecyclerView.ViewHolder(
    binding.root) {
    fun onBind(data:  ChatDTO.ChatData) {
        binding.itemchatbubble = data
    }
}