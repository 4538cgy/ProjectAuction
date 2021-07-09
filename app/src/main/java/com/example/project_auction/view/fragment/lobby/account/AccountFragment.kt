package com.example.project_auction.view.fragment.lobby.account

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.project_auction.R
import com.example.project_auction.base.BaseFragment
import com.example.project_auction.databinding.FragmentAccountBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AccountFragment : BaseFragment<FragmentAccountBinding>(R.layout.fragment_account) { //Fragment() {

    private val profileCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it != null) {
            if (it.data != null){
                photoUri = it.data!!.data.toString()
                Glide.with(binding.root.context)
                    .load(it.data!!.data)
                    .circleCrop()
                    .into(binding.fragmentAccountImageviewProfile)

                loginSignViewModel.profilePhotoUri.postValue(it.data!!.data.toString())
            }
        }
    }

    lateinit var photoUri : String
    val REQUEST_IMAGE_CAPTURE = 1
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
                        1 -> {
                            //settingPermission() // 권한체크 시작
                            startCapture()
                        }
                    }

                }.show()
                update()
            }
        }
    }

    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        profileCallback.launch(intent)
    }

    fun update(){
        //개인정보 db 업데이트
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
                //ActivityCompat.finishAffinity(this@MainActivity) // 권한 거부시 앱 종료
                //System.exit(0)
            }
        }

        TedPermission.with(requireContext())
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
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
                    //startForResult.launch(takePictureIntent)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    // 이미지뷰에 내가 촬영한 사진 표시
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val file = File(currentPhotoPath)
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media
                    .getBitmap(requireContext().contentResolver, Uri.fromFile(file))
                Glide.with(binding.root.context)
                    .load(currentPhotoPath)
                    .circleCrop()
                    .into(binding.fragmentAccountImageviewProfile)

                //binding.imgPicture.setImageBitmap(bitmap)
            }
            else{
                val decode = ImageDecoder.createSource(requireContext().contentResolver,
                    Uri.fromFile(file))
                val bitmap = ImageDecoder.decodeBitmap(decode)
                Glide.with(binding.root.context)
                    .load(currentPhotoPath)
                    .circleCrop()
                    .into(binding.fragmentAccountImageviewProfile)
                //binding.imgPicture.setImageBitmap(bitmap)
            }
        }
    }

}