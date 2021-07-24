package com.example.project_auction.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.project_auction.R
import com.example.project_auction.databinding.BottomSheetPostOptionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.ClassCastException

class BottomSheetPostOption : BottomSheetDialogFragment() {
    lateinit var bottomSheetButtonClickListener: BottomSheetSetCloseProductButtonClickListener

    lateinit var binding: BottomSheetPostOptionBinding

    //교환 가능 여부
    private var transState = ""
    //직거래 여부
    private var tradeState = ""
    //제품 상태
    private var productState = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_post_option, container, false
        )
        binding.bottomsheetpostoption = this


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomSheetPostOptionImagebuttonClose.setOnClickListener {
            dismiss()
        }

        binding.bottomSheetPostOptionButtonComplete.setOnClickListener {
            bottomSheetButtonClickListener.onBottomSheetSetCloseProductButtonClick(tradeState,productState,transState)
            dismiss()
        }

        //제품 상태
        binding.bottomSheetPostOptionRadioGroupProductState.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.bottom_sheet_post_option_radiobutton_new -> { productState = "새상품" }
                R.id.bottom_sheet_post_option_radiobutton_old -> { productState = "중고상품" }
            }
        }

        //직거래 여부
        binding.bottomSheetPostOptionRadioGroupTradeOption.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.bottom_sheet_post_option_radiobutton_meet -> { tradeState = "직거래" }
                R.id.bottom_sheet_post_option_radiobutton_delivery -> { tradeState = "택배거래"}
            }
        }

        //교환 가능 여부
        binding.bottomSheetPostOptionRadioGroupTrans.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.bottom_sheet_post_option_radiobutton_tans_noable -> { transState = "교환 불가능"}
                R.id.bottom_sheet_post_option_radiobutton_trans_able -> { transState = "교환 가능" }
            }
        }
    }


    interface BottomSheetSetCloseProductButtonClickListener {
        fun onBottomSheetSetCloseProductButtonClick(tradeOption : String, productState : String, transState : String)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            bottomSheetButtonClickListener =
                context as BottomSheetSetCloseProductButtonClickListener

        } catch (e: ClassCastException) {
            Log.d("bottomsheet", "Click listener onAttach Error")
        }
    }
}