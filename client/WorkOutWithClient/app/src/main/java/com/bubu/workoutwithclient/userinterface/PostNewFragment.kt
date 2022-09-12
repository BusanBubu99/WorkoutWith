package com.bubu.workoutwithclient.userinterface

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.FragmentPostNewBinding
import com.bubu.workoutwithclient.retrofitinterface.UserCreateCommunityData
import com.bubu.workoutwithclient.retrofitinterface.UserCreateCommunityModule
import com.bubu.workoutwithclient.retrofitinterface.UserError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


suspend fun postCommunity(title: String, picture: File?, content: String) {
    val postObject = UserCreateCommunityModule(UserCreateCommunityData(title, picture, content))
    val result = postObject.getApiData()
    if (result in 200..299) {
        Log.d("Community", "Successful!")
    } else if (result is UserError) {

    } else {

    }
}

class PostNewFragment : Fragment() {

    lateinit var majorScreen: MajorScreen
    lateinit var binding: FragmentPostNewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostNewBinding.inflate(inflater, container, false)
        binding.btnUploadCamera.setOnClickListener {
            openDialog(majorScreen)
        }
        binding.btnPost.setOnClickListener {
            if (binding.editPostTitle.text.isNotEmpty() && binding.editPostContent.text.isNotEmpty()) {
                CoroutineScope(Dispatchers.Default).launch {
                    //Log.d("filePath", filePath)
                    if (filePath != "") {
                        Log.d("fileEXis",filePath)
                        postCommunity(
                            binding.editPostTitle.text.toString(),
                            File(filePath),
                            binding.editPostContent.text.toString()
                        )
                    } else {
                        Log.d("nofile",filePath)
                        postCommunity(
                            binding.editPostTitle.text.toString(),
                            null,
                            binding.editPostContent.text.toString()
                        )
                    }

                }
                val direction =
                    PostNewFragmentDirections.actionPostNewFragmentToCommunityFragment()
                findNavController().navigate(direction)
            }
        }
        return binding.root
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

    var filePath = ""
    private val getContentImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri.let {
                binding.imagePostView.setImageURI(uri)
                val uriPathHelper = URIPathHelper()
                if (uri != null)
                    filePath =
                        this.context?.let { it1 -> uriPathHelper.getPath(it1, uri!!) }.toString()
                Log.d("umm", filePath)
            }
        }

    var pictureUri: Uri? = null
    private val getTakePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            pictureUri.let {
                binding.imagePostView.setImageURI(pictureUri)
                val uriPathHelper = URIPathHelper()
                if (pictureUri != null)
                    filePath = this.context?.let { it1 -> uriPathHelper.getPath(it1, pictureUri!!) }
                        .toString()
            }
        }
    }

    private val getTakePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap.let { binding.imagePostView.setImageBitmap(bitmap) }
        }

    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val requestMultiplePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.forEach {
                if (!it.value) {
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
        return majorScreen?.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            content
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        majorScreen = context as MajorScreen
    }
}