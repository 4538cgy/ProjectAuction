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
            //?????? ?????????
            bottomSheetDialogBiddingImagebuttonMinus.setOnClickListener {
                currentCost -= getMinBiddingCost()
                updateWantCostTextview()
            }
            //?????? ?????????
            bottomSheetDialogBiddingImagebuttonPlus.setOnClickListener {
                currentCost += getMinBiddingCost()
                updateWantCostTextview()
            }
            //?????? ??????
            bottomSheetDialogBiddingButtonBidding.setOnClickListener {

                if (startCost < currentCost) {

                    var builder = AlertDialog.Builder(binding.root.context)

                    builder.apply {
                        setMessage("????????? ${currentCost}???\n?????? ??? ?????? ????????? ????????? ??? ????????????.\n??? ????????? ?????????????????????????")
                        setPositiveButton("???", DialogInterface.OnClickListener { dialog, which ->
                            bottomSheetButtonClickListener.onBottomSheetButtonClick(currentCost.toString())
                            dismiss()
                        })
                        setNegativeButton("?????????", DialogInterface.OnClickListener { dialog, which ->

                        })
                        setTitle("[!!??????!!]")
                        show()
                    }
                }else{
                    Toast.makeText(binding.root.context, "????????? ??? ?????? ???????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show()
                }

            }
            //??????
            bottomSheetDialogBiddingImagebuttonClose.setOnClickListener {
                dismiss()
            }
            //?????? ?????? ??????
            bottomSheetDialogBiddingTextviewCostSegment.text = "?????? ?????? ?????? : " + getMinBiddingCost().toString() + "???"

            //?????? ?????????
            bottomSheetDialogBiddingTextviewCurrentCost.text = "?????? ????????? : " + currentCost.toString() + "???"

            //?????? ?????????
            updateWantCostTextview()
        }
    }

    fun updateWantCostTextview(){
        binding.bottomSheetDialogBiddingTextviewWantCost.text = "?????? ?????? ??????\n" + currentCost + "???"
    }

    fun getMinBiddingCost() : Long {
        println("?????????")
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