package com.example.project_auction.view.bottomsheet

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_auction.R
import com.example.project_auction.data.BottomSheetRecyclerDTO
import com.example.project_auction.databinding.BottomSheetDialogBiddingBinding
import com.example.project_auction.databinding.BottomSheetDialogCategoryBinding
import com.example.project_auction.databinding.ItemBottomSheetCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.ClassCastException
import kotlin.properties.Delegates

class BottomSheetBidding : BottomSheetDialogFragment() {
    lateinit var bottomSheetButtonClickListener: BottomSheetButtonClickListener

    lateinit var binding: BottomSheetDialogBiddingBinding

    var startCost by Delegates.notNull<Long>()
    var currentCost by Delegates.notNull<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_dialog_bidding, container, false
        )
        binding.bottomsheetdialogbidding = this

        val bundle = arguments
        currentCost = bundle!!.getString("currentCost").toString().replace(",","").toRegex().toString().toLong()
        startCost = currentCost

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            //가격 내리기
            bottomSheetDialogBiddingImagebuttonMinus.setOnClickListener {
                currentCost -= getMinBiddingCost()
                updateWantCostTextview()
            }
            //가격 올리기
            bottomSheetDialogBiddingImagebuttonPlus.setOnClickListener {
                currentCost += getMinBiddingCost()
                println("으아아 올리기 $currentCost")
                updateWantCostTextview()
            }
            //입찰 확정
            bottomSheetDialogBiddingButtonBidding.setOnClickListener {

                if (startCost < currentCost) {

                    var builder = AlertDialog.Builder(binding.root.context)

                    builder.apply {
                        setMessage("입찰가 ${currentCost}원\n입찰 후 입찰 내역은 변경할 수 없습니다.\n이 가격에 입찰하시겠습니까?")
                        setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                            bottomSheetButtonClickListener.onBottomSheetButtonClick(currentCost.toString())
                            dismiss()
                        })
                        setNegativeButton("아니요", DialogInterface.OnClickListener { dialog, which ->

                        })
                        setTitle("[!!주의!!]")
                        show()
                    }
                }else{
                    Toast.makeText(binding.root.context, "같거나 더 낮은 가격으로 입찰할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }

            }
            //끄기
            bottomSheetDialogBiddingImagebuttonClose.setOnClickListener {
                dismiss()
            }
            //최소 입찰 단위
            bottomSheetDialogBiddingTextviewCostSegment.text = "최소 입찰 단위 : " + getMinBiddingCost().toString() + "원"

            //현재 입찰가
            bottomSheetDialogBiddingTextviewCurrentCost.text = "현재 입찰가 : " + currentCost.toString() + "원"

            //희망 입찰가
            updateWantCostTextview()
        }
    }

    fun updateWantCostTextview(){
        binding.bottomSheetDialogBiddingTextviewWantCost.text = "희망 입찰 금액\n" + currentCost + "원"
    }

    fun getMinBiddingCost() : Long {
        println("실행됨")
        var cost : Long = 0
        when(currentCost){
            in 0..10000 -> {
                println("500")
                cost = 500
            }
            in 10000..100000 ->{

                println("1000")
                cost = 1000
            }
            in 100000..1000000 ->{

                println("10000")
                cost =10000
            }
            in 1000000..100000000 ->{

                println("100000")
                cost = 100000
            }
            in 100000000..99999999999 ->{
                println("1000000")
                cost = 1000000
            }
        }
        return cost
    }



    interface BottomSheetButtonClickListener {
        fun onBottomSheetButtonClick(text: String)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            bottomSheetButtonClickListener = context as BottomSheetButtonClickListener

        } catch (e: ClassCastException) {
            Log.d("bottomsheet", "Click listener onAttach Error")
        }
    }



}