package com.example.project_auction.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_auction.R
import com.example.project_auction.data.BottomSheetRecyclerDTO
import com.example.project_auction.databinding.BottomSheetDialogCategoryBinding
import com.example.project_auction.databinding.ItemBottomSheetCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.ClassCastException

class BottomSheetCategory : BottomSheetDialogFragment() {
    lateinit var bottomSheetButtonClickListener: BottomSheetButtonClickListener

    lateinit var binding: BottomSheetDialogCategoryBinding

    private var categoryList = arrayListOf<BottomSheetRecyclerDTO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_dialog_category, container, false
        )
        binding.bottomsheetdialogcategory = this
        initRecyclerData()

        binding.bottomSheetDialogCategoryRecycler.adapter = CategoryAdapter()
        binding.bottomSheetDialogCategoryRecycler.layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomSheetDialogCategoryRecycler.adapter!!.notifyDataSetChanged()
    }

    fun initRecyclerData(){
        println("리사이클러뷰 부착")

            categoryList.add(BottomSheetRecyclerDTO(R.drawable.close_btn_img,"닫기"))
            categoryList.add(BottomSheetRecyclerDTO(R.drawable.ic_baseline_search_24,"검색"))

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




    inner class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val binding = ItemBottomSheetCategoryBinding.inflate(LayoutInflater.from(binding.root.context),parent,false)
            return CategoryViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.onBind(categoryList[position])

            holder.itemView.setOnClickListener {
                println(categoryList[position].text)
                bottomSheetButtonClickListener.onBottomSheetButtonClick(categoryList[position].text.toString())
                dismiss()
            }

            holder.binding.itemBottomSheetCategoryImageview.setImageResource(categoryList[position].drawable!!)
            holder.binding.itemBottomSheetCategoryTextview.text = categoryList[position].text
        }



        override fun getItemCount(): Int {
            return if (categoryList != null){
                categoryList.size
            }else{
                0
            }
        }

        inner class CategoryViewHolder(val binding : ItemBottomSheetCategoryBinding) : RecyclerView.ViewHolder(binding.root){
            fun onBind(data : BottomSheetRecyclerDTO) {
                binding.itembottomsheetcategory = data
            }

        }

    }
}