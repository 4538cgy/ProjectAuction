package com.example.project_auction.view.fragment.lobby.account

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.adapter.SettingAdapter
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.data.SettingItem
import com.example.project_auction.data.SettingType
import com.example.project_auction.databinding.FragmentAccountBinding


class AccountFragment : BaseFragment<FragmentAccountBinding>(R.layout.fragment_account) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentaccount = this
        binding.profileviewmodel = profileViewModel

        binding.apply {

            fragmentAccountRecyclerSetting.adapter = SettingAdapter(
                arrayListOf(
                    SettingItem(SettingType.TITLE, R.drawable.ic_baseline_add_24, "제목",true),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "로그아웃"),
                    SettingItem(SettingType.TITLE, R.drawable.ic_baseline_add_24, "제목",true),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "로그아웃"),
                    SettingItem(SettingType.TITLE, R.drawable.ic_baseline_add_24, "제목",true),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "로그아웃"),
                    SettingItem(SettingType.TITLE, R.drawable.ic_baseline_add_24, "제목",true),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "로그아웃"),
                    SettingItem(SettingType.TITLE, R.drawable.ic_baseline_add_24, "제목",true),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "로그아웃")

                )
            ).setOnSettingItemClickListener {

            }
            fragmentAccountRecyclerSetting.layoutManager = LinearLayoutManager(root.context,LinearLayoutManager.VERTICAL,false)
        }

        //닉네임 옵저빙
        profileViewModel.getUserNickName(auth.currentUser!!.uid)
        profileViewModel.nickName.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.fragmentAccountTextviewNickname.text = it.toString()
        })

        //프로필 이미지 옵저빙
        profileViewModel.getProfileImage(auth.currentUser!!.uid)
        profileViewModel.profileImage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Glide.with(binding.root.context)
                .load(it)
                .circleCrop()
                .thumbnail(0.1f)
                .into(binding.fragmentAccountImageviewProfile)
        })

    }


}