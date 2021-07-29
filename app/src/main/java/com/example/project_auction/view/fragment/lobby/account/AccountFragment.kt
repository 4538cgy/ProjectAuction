package com.example.project_auction.view.fragment.lobby.account

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.databinding.FragmentAccountBinding
import com.example.project_auction.util.fcm.FcmPush
import com.example.project_auction.view.activity.history.AuctionHistoryActivity
import com.example.project_auction.view.activity.history.AuctionJoinHistoryActivity
import com.example.project_auction.view.activity.history.AuctionSuccessHistoryActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.fragment_account.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AccountFragment : BaseFragment<FragmentAccountBinding>(R.layout.fragment_account) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentaccount = this
        binding.profileviewmodel = profileViewModel

        binding.apply {

            fragmentAccountTextviewAuctionJoin.setOnClickListener {
                val message = "으아아아아"
                FcmPush().sendMessage("u27csIcJR4RH82waiVG9txHn2FI3","으아아아아아아",message)
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