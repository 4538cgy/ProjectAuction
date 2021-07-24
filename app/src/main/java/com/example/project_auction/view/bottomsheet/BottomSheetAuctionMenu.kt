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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_auction.R
import com.example.project_auction.data.BottomSheetRecyclerDTO
import com.example.project_auction.databinding.BottomSheetAuctionMenuBinding
import com.example.project_auction.databinding.BottomSheetDialogBiddingBinding
import com.example.project_auction.databinding.ItemBottomSheetCategoryBinding
import com.example.project_auction.viewmodel.AuctionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.ClassCastException
import kotlin.properties.Delegates

class BottomSheetAuctionMenu: BottomSheetDialogFragment() {
    lateinit var bottomSheetButtonClickListener: BottomSheetButtonClickListener

    private val viewModel : AuctionViewModel by viewModels()


    lateinit var binding: BottomSheetAuctionMenuBinding
    private var categoryList = arrayListOf<BottomSheetRecyclerDTO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_auction_menu, container, false
        )
        binding.bottomsheetauctionmenu = this
        initRecyclerData()
        binding.apply {
            bottomSheetAuctionMenuRecycler.adapter = CategoryAdapter()
            bottomSheetAuctionMenuRecycler.layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)

            bottomSheetAuctionMenuImagebuttonClose.setOnClickListener {
                dismiss()
            }
        }

        return binding.root
    }

    fun initRecyclerData() {
        println("리사이클러뷰 부착")

        categoryList.add(BottomSheetRecyclerDTO(R.drawable.close_btn_img, "관상어"))
        categoryList.add(BottomSheetRecyclerDTO(R.drawable.ic_baseline_search_24, "수조용품"))
        categoryList.add(BottomSheetRecyclerDTO(R.drawable.ic_baseline_search_24, "수초"))
        categoryList.add(BottomSheetRecyclerDTO(R.drawable.ic_baseline_search_24, "수조"))
        categoryList.add(BottomSheetRecyclerDTO(R.drawable.ic_baseline_search_24, "관상새우"))
        categoryList.add(BottomSheetRecyclerDTO(R.drawable.ic_baseline_search_24, "수조용품"))
        categoryList.add(BottomSheetRecyclerDTO(R.drawable.ic_baseline_search_24, "수조용품"))
        categoryList.add(BottomSheetRecyclerDTO(R.drawable.ic_baseline_search_24, "수조용품"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {


        }
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

    inner class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val binding = ItemBottomSheetCategoryBinding.inflate(
                LayoutInflater.from(binding.root.context),
                parent,
                false
            )
            return CategoryViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.onBind(categoryList[position])

            holder.itemView.setOnClickListener {
                println(categoryList[position].text)
                viewModel.auctionCategory.postValue(categoryList[position].text)
                dismiss()
            }

            holder.binding.itemBottomSheetCategoryImageview.setImageResource(categoryList[position].drawable!!)
            holder.binding.itemBottomSheetCategoryTextview.text = categoryList[position].text
        }


        override fun getItemCount(): Int {
            return if (categoryList != null) {
                categoryList.size
            } else {
                0
            }
        }

        inner class CategoryViewHolder(val binding: ItemBottomSheetCategoryBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun onBind(data: BottomSheetRecyclerDTO) {
                binding.itembottomsheetcategory = data
            }

        }

    }


}