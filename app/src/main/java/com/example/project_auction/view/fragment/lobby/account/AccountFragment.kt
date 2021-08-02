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
                    SettingItem(SettingType.TITLE, R.drawable.ic_baseline_add_24, "경매", true),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "경매 낙찰 내역"),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "내 경매품 내역"),
                    SettingItem(SettingType.TITLE, R.drawable.ic_baseline_add_24, "거래", true),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "구매 내역"),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "판매 내역"),
                    SettingItem(SettingType.TITLE, R.drawable.ic_baseline_add_24, "혜택", true),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "이벤트"),
                    SettingItem(SettingType.TITLE, R.drawable.ic_baseline_add_24, "더보기", true),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "앱 설정"),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "계정 설정"),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "공지사항"),
                    SettingItem(SettingType.BUY_LIST, R.drawable.ic_baseline_add_24, "문의하기")

                    )
            ).setOnSettingItemClickListener {
                when(this.title){
                    "경매 낙찰 내역" ->{

                    }
                    "내 경매품 내역" ->{

                    }
                    "구매 내역" ->{

                    }
                    "판매 내역" ->{

                    }
                    "이벤트" ->{

                    }
                    "앱 설정" ->{

                    }
                    "계정 설정"->{

                    }
                    "공지사항" ->{

                    }
                    "문의하기" ->{

                    }
                }
            }
            fragmentAccountRecyclerSetting.layoutManager =
                LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)

            fragmentAccountConstraintNicknameContainer.setOnClickListener {
                //프로필 변경 todo

            }
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