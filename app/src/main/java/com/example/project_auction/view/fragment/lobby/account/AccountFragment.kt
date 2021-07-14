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
import com.example.project_auction.view.activity.history.RegistrationHistoryActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.fragment_account.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AccountFragment : BaseFragment<FragmentAccountBinding>(R.layout.fragment_account) {

    private val profileCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it != null) {
            if (it.data != null ){
                photoUri = it.data!!.data.toString()
                Glide.with(binding.root.context)
                    .load(it.data!!.data)
                    .circleCrop()
                    .into(binding.fragmentAccountImageviewProfile)

                loginSignViewModel.profilePhotoUri.postValue(it.data!!.data.toString())
            }
            else if(it.resultCode == Activity.RESULT_OK) {
                val file = File(currentPhotoPath)
                Glide.with(binding.root.context)
                    .load(currentPhotoPath)
                    .circleCrop()
                    .into(binding.fragmentAccountImageviewProfile)
            }
        }
    }

    lateinit var photoUri : String
    lateinit var currentPhotoPath : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentaccount = this

        binding.apply {
            fragmentAccountImageviewProfile.setOnClickListener{
                settingPermission()
                var selectImage = arrayOf("앨범에서 선택","사진 촬영")
                val builder = AlertDialog.Builder(requireContext())
                .setTitle("프로필 사진")
                .setItems(selectImage) { dialog, which ->
                    when(which){
                        0 -> openGallery()
                        1 -> startCapture()
                    }

                }.show()
                update()
            }

            // NickName Change Event 처리
            fragment_account_textview_nickname.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                val dialogView = layoutInflater.inflate(R.layout.alertdialog_edittext, null)
                val dialogText = dialogView.findViewById<EditText>(R.id.alertdialog_edittext_nickname)

                builder.setView(dialogView)
                    .setPositiveButton("확인") { dialogInterface, i ->
                        if (dialogText.text.isBlank()) {
                            Toast.makeText(
                                requireContext(),
                                "공백 또는 띄어쓰기가 불가합니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setPositiveButton
                        }
                        fragment_account_textview_nickname.text = dialogText.text.toString().replace(" ","")
                    }
                    .setNegativeButton("취소") { dialogInterface, i ->
                        /* 취소일 때 아무 액션이 없으므로 빈칸 */
                    }

                // dismiss를 사용하기 위하여 처리과정 변경
                val dlgselect = builder.create()
                dlgselect.show()

                // Enter Event 처리
                dialogText.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KEYCODE_ENTER && dialogText.text.isNotBlank()){
                        fragment_account_textview_nickname.text = dialogText.text.toString().replace(" ","")
                        dlgselect.dismiss()
                        true
                    }
                    true
                }
                update()
            }

            fragmentAccountButtonAuctionHistory.setOnClickListener {
                val intent = Intent(requireContext(),RegistrationHistoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        profileCallback.launch(intent)
    }

    fun update(){
        //개인정보 db 업데이트(프로필 사진, 닉네임)
    }

    //권한 설정
    fun settingPermission(){
        var permis = object  : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                Toast.makeText(requireContext(), "권한 허가", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(requireContext(), "권한 거부", Toast.LENGTH_SHORT)
                    .show()
                ActivityCompat.finishAffinity(activity!!) // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(requireContext())
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA)
            .check()
    }

    // 사진 촬영 후 저장
    @Throws(IOException::class)
    private fun createImageFile() : File {
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply{
            currentPhotoPath = absolutePath
        }
    }

    // 카메라 호출
    fun startCapture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try{
                    createImageFile()
                }catch(ex: IOException){
                    null
                }
                photoFile?.also{
                    val photoURI : Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.project_auction.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    profileCallback.launch(takePictureIntent)
                }
            }
        }
    }
}