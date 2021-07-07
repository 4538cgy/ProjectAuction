package com.example.project_auction.view.fragment.signup

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.project_auction.R
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.data.NickNameDTO
import com.example.project_auction.databinding.FragmentSignUpSecondBinding
import com.google.firebase.firestore.FirebaseFirestore

class SignUpSecondFragment : BaseFragment<FragmentSignUpSecondBinding>(R.layout.fragment_sign_up_second) {

    private var nickCheck : Boolean = false
    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentsignupsecond = this

        binding.apply {
            fragmentSignUpSecondEdittextNick.addTextChangedListener {
                updateView(it!!.length)
            }
            fragmentSignUpSecondButtonNickCheck.setOnClickListener {

                /*
                var dataReference = db.collection("nickName").document("nickList")
                db.runTransaction {
                    var dataList = it.get(dataReference).toObject(NickNameDTO::class.java)

                    dataList!!.nickNameList.put(binding.fragmentSignUpSecondEdittextNick.text.toString(),true)
                    println("트랜잭션")
                    it.set(dataReference,dataList)
                    return@runTransaction
                }

                 */

                db.collection("nickName").document("nickList").get().addOnSuccessListener {
                    if (it != null){
                        if (it.exists()){
                            var nickList = it.toObject(NickNameDTO::class.java)

                            if (nickList!!.nickNameList.containsKey(binding.fragmentSignUpSecondEdittextNick.text.toString())){
                                Toast.makeText(binding.root.context, "닉네임이 이미 존재합니다.", Toast.LENGTH_SHORT).show()
                            }else{
                                var builder = AlertDialog.Builder(binding.root.context)

                                builder.apply {
                                    setMessage("닉네임이 존재하지 않습니다. 해당 닉네임으로 진행하시겠습니까?")
                                    setPositiveButton("예" , DialogInterface.OnClickListener { dialog, which ->
                                        binding.fragmentSignUpSecondEdittextNick.isEnabled = false
                                        loginSignViewModel.nickNameNextButtonState.postValue(true)
                                    })
                                    setNegativeButton("아니요" , DialogInterface.OnClickListener { dialog, which ->
                                        binding.fragmentSignUpSecondEdittextNick.setText("")
                                    })
                                    setTitle("안내")
                                    show()
                                }
                            }
                        }
                    }
                }
                

            }
        }
    }

    fun updateView(count : Int){
        binding.fragmentSignUpSecondTextviewTextCount.text = "$count/10"

        if (count > 1){
            binding.fragmentSignUpSecondButtonNickCheck.apply {
                isEnabled = true
                setBackgroundResource(R.drawable.background_round_black_4dp)
                setTextColor(Color.WHITE)
            }
        }else{
            binding.fragmentSignUpSecondButtonNickCheck.apply {
                isEnabled = false
                setBackgroundResource(R.drawable.background_round_gray_stroke_black_4dp)
                setTextColor(Color.BLACK)
            }
        }
    }

}