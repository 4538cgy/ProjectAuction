package com.example.project_auction.view.activity.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_auction.R
import com.example.project_auction.adapter.ChatAdapter
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.data.ChatDTO
import com.example.project_auction.data.ProductAuctionDTO
import com.example.project_auction.data.ProductTradeDTO
import com.example.project_auction.data.UserDTO
import com.example.project_auction.databinding.ActivityTradeChatBinding
import com.example.project_auction.extension.toast

class TradeChatActivity : BaseActivity<ActivityTradeChatBinding>(R.layout.activity_trade_chat) {

    //채팅을 당하는 Uid
    private var destinationUid: String? = null

    private var productData = ProductTradeDTO()
    private var productDataId: String? = null

    private var chatRoomId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activitytradechat = this

        destinationUid = intent.getStringExtra("destinationUid").toString()
        productData = intent.getSerializableExtra("productData") as ProductTradeDTO
        productDataId = intent.getStringExtra("productDataId")
        getUserName()
        checkChatRoom()

        binding.apply {
            //메세지 보내기
            activityTradeChatImagebuttonSend.setOnClickListener {
                sendMessage()
                activityTradeChatEdittextChat.setText("")
            }
            //뒤로가기
            activityTradeChatImagebuttonClose.setOnClickListener {
                finish()
            }

            //판매 상태
            if (productData.delete) {
                activityTradeChatTextviewProductState.text = "거래 완료"
            } else {
                activityTradeChatTextviewProductState.text = "판매 중"
            }

            //제목
            activityTradeChatTextviewTitle.text = productData.title
        }
    }

    fun getUserName() {
        db.collection("User").whereEqualTo("uid", destinationUid)
            .addSnapshotListener { value, error ->
                value?.let {
                    //none - null todo
                    if (!it.isEmpty) {
                        val datas = it.toObjects(UserDTO::class.java)
                        datas.forEach { userData ->
                            if (userData.uid.equals(destinationUid)) {
                                binding.activityTradeChatTextviewNickname.text = userData.nickName

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

    private fun sendMessage() {
        //로그인 되어있는지 검사
        if (auth.currentUser != null) {
            //채팅방이 없으면 생성
            if (chatRoomId == null) {
                createChatRoom()
            } else {
                //채팅방이 있으면 해당 채팅방에 트랜잭션
                updateChatMessage()
            }
        }
    }

    private fun createChatRoom() {
        var chatData = ChatDTO()
        chatData.users.put(auth.currentUser!!.uid, true)
        chatData.users.put(destinationUid!!, true)
        chatData.postId = productDataId
        chatData.lastTimestamp = System.currentTimeMillis()

        var chatDetailData = ChatDTO.ChatData()
        chatDetailData.message = binding.activityTradeChatEdittextChat.text.toString()
        chatDetailData.timestamp = System.currentTimeMillis()
        chatDetailData.uid = auth.currentUser!!.uid
        chatData.comments.add(chatDetailData)

        db.collection("Chat").document().set(chatData).addOnSuccessListener {
            println("성공")
            checkChatRoom()
        }.addOnFailureListener {
            println("실패 ${it.toString()}")
        }
    }

    private fun updateChatMessage() {
        val databaseReference = db.collection("Chat").document(chatRoomId.toString())
        db.runTransaction { transaction ->

            var chat = transaction.get(databaseReference).toObject(ChatDTO::class.java)!!
            chat.lastTimestamp
            var chatDetailData = ChatDTO.ChatData()
            chatDetailData.message = binding.activityTradeChatEdittextChat.text.toString()
            chatDetailData.timestamp = System.currentTimeMillis()
            chatDetailData.uid = auth.currentUser!!.uid
            chat.comments.add(chatDetailData)

            transaction.set(databaseReference, chat)
            return@runTransaction
        }.addOnCompleteListener {
            println("현재 입찰가 수정완료")
        }.addOnFailureListener {
            println("현재 입찰가 수정실패 사유 : ${it.toString()}")
        }
    }

    private fun checkChatRoom() {
        db.collection("Chat").whereEqualTo("postId", productDataId)
            .whereEqualTo("users." + auth.currentUser!!.uid, true)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    value.documents.forEach {
                        chatRoomId = it.id
                        var chatData = it.toObject(ChatDTO::class.java)
                        if (chatData!!.users.containsKey(destinationUid) && chatData!!.users.containsKey(
                                auth.currentUser!!.uid
                            )
                        ) {
                            binding.activityTradeChatRecycler.apply {
                                adapter = ChatAdapter(
                                    binding.root.context, chatData!!.comments,
                                    destinationUid.toString(), auth.currentUser!!.uid
                                )
                                layoutManager = LinearLayoutManager(
                                    binding.root.context,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            }
                        }

                    }
                }
            }
    }
}