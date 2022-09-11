package com.bubu.workoutwithclient.userinterface

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.FragmentEditProfileBinding
import java.text.SimpleDateFormat
import java.util.*


class EditProfileFragment : Fragment() {

    lateinit var binding : FragmentEditProfileBinding
    lateinit var majorScreen: MajorScreen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        majorScreen?.hideNavBar(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        requestMultiplePermission.launch(permissionList)
        binding.editProfileImage.setOnClickListener {
            openDialog(majorScreen)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEditComplete.setOnClickListener {
            val editId = binding.EditProfileId.text.toString()
            setFragmentResult("request", bundleOf("editId" to editId))
            val editContent = binding.EditProfileContent.text.toString()
            setFragmentResult("request", bundleOf("editContent" to editContent))
            val direction = EditProfileFragmentDirections.actionEditProfileFragment2ToMyProfileFragment()
            findNavController().navigate(direction)
        }
        setFragmentResultListener("request") { key, bundle ->
            bundle.getString("profileId")?.let {
                binding.EditProfileId.setText(it)
            }
            bundle.getString("profileContent")?.let {
                binding.EditProfileContent.setText(it)
            }
        }
    }

    private fun openDialog(context: Context) {
        val dialogLayout = layoutInflater.inflate(R.layout.custom_dialog, null)
        val dialogBuild = AlertDialog.Builder(context).apply {
            setView(dialogLayout)
        }
        val dialog = dialogBuild.create().apply { show() }

        val cameraAddBtn = dialogLayout.findViewById<Button>(R.id.btnCamera)
        val fileAddBtn = dialogLayout.findViewById<Button>(R.id.btnGallery)

        cameraAddBtn.setOnClickListener {
            // 1. TakePicture
            pictureUri = createImageFile()
            getTakePicture.launch(pictureUri)   // Require Uri

            // 2. TakePicturePreview
//            getTakePicturePreview.launch(null)    // Bitmap get

            dialog.dismiss()
        }
        fileAddBtn.setOnClickListener {
            getContentImage.launch("image/*")
            dialog.dismiss()
        }
    }
    private val getContentImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri.let { binding.editProfileImage.setImageURI(uri) }
    }

    var pictureUri: Uri? = null
    private val getTakePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if(it) {
            pictureUri.let { binding.editProfileImage.setImageURI(pictureUri) }
        }
    }

    private val getTakePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap.let { binding.editProfileImage.setImageBitmap(bitmap) }
    }

    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE)

    private val requestMultiplePermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        results.forEach {
            if(!it.value) {
                Toast.makeText(majorScreen, "권한 허용 필요", Toast.LENGTH_SHORT).show()
                majorScreen?.finish()
            }
        }
    }

    private fun createImageFile(): Uri? {
        val now = SimpleDateFormat("yyMMdd_HHmmss").format(Date())
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return majorScreen?.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        majorScreen = context as MajorScreen
    }

    override fun onDestroy() {
        Log.d("프래그먼트 종료", "프래그먼트 종료")
        super.onDestroy()
        majorScreen?.hideNavBar(false)
    }
}