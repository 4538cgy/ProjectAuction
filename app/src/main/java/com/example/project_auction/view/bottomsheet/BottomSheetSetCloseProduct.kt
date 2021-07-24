package com.example.project_auction.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_auction.R
import com.example.project_auction.data.BottomSheetRecyclerDTO
import com.example.project_auction.databinding.BottomSheetDialogCategoryBinding
import com.example.project_auction.databinding.BottomSheetDialogSetcloseproductBinding
import com.example.project_auction.databinding.ItemBottomSheetCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.ClassCastException

class BottomSheetSetCloseProduct: BottomSheetDialogFragment() {
    lateinit var bottomSheetButtonClickListener: BottomSheetSetCloseProductButtonClickListener

    lateinit var binding: BottomSheetDialogSetcloseproductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL ,R.style.DialogStyle)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_dialog_setcloseproduct, container, false
        )
        binding.bottomsheetdialogsetcloseproduct = this


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomSheetDialogSetcloseproductButtonSet.setOnClickListener {
            bottomSheetButtonClickListener.onBottomSheetSetCloseProductButtonClick(binding.bottomSheetDialogSetcloseproductEdittextDay.text.toString())
            dismiss()
        }
    }



    interface BottomSheetSetCloseProductButtonClickListener {
        fun onBottomSheetSetCloseProductButtonClick(text: String)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            bottomSheetButtonClickListener = context as BottomSheetSetCloseProductButtonClickListener

        } catch (e: ClassCastException) {
            Log.d("bottomsheet", "Click listener onAttach Error")
        }
    }



}