package com.example.project_auction.view.activity.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.data.ChatDTO
import com.example.project_auction.databinding.ActivityTradeChatBinding

class TradeChatActivity : BaseActivity<ActivityTradeChatBinding>(R.layout.activity_trade_chat) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activitytradechat = this

        binding.apply {
            //메세지 보내기
            activityTradeChatImagebuttonSend.setOnClickListener {
                sendMessage()
            }

            //입력 메세지
            activityTradeChatEdittextChat.addTextChangedListener {

            }
        }
    }

    private fun sendMessage(){

        var chatData = ChatDTO()
        chatData.users.put("aaaa",true)
        chatData.users.put("bbbb",true)
        chatData.lastTimestamp = System.currentTimeMillis()

        var chatDetailData = ChatDTO.ChatData()
        chatDetailData.message = binding.activityTradeChatEdittextChat.text.toString()
        chatDetailData.timestamp = System.currentTimeMillis()
        chatDetailData.uid = auth.currentUser!!.uid
        chatData.comments.add(chatDetailData)

        db.collection("Chat").document().set(chatData).addOnSuccessListener {
            println("성공")
        }.addOnFailureListener {
            println("실패 ${it.toString()}")
        }
    }
}