package com.bubu.workoutwithclient.userinterface.profile

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.ProfileEditFragmentBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import com.bubu.workoutwithclient.userinterface.MainScreenActivity
import com.bubu.workoutwithclient.userinterface.login.URIPathHelper
import com.bubu.workoutwithclient.userinterface.login.editProfile
import com.bubu.workoutwithclient.userinterface.match.downloadProfilePic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path =
        MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
    return Uri.parse(path)
}

class ProfileEditFragment : Fragment() {
    var filePath = ""
    lateinit var binding : ProfileEditFragmentBinding
    lateinit var mainScreenActivity: MainScreenActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainScreenActivity?.hideNavBar(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProfileEditFragmentBinding.inflate(inflater, container, false)
        requestMultiplePermission.launch(permissionList)
        binding.editProfileImage.setOnClickListener {
            openDialog(mainScreenActivity)
        }
        return binding.root
    }

    override fun onResume() {
        mainScreenActivity?.setTitle("프로필 편집하기")
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lateinit var bitmap : Bitmap

        CoroutineScope(Dispatchers.Default).launch {
            val getProfileObject = UserGetProfileModule(UserGetProfileData(userInformation.userId))
            val result = getProfileObject.getApiData()
            if(result is UserGetProfileResponseData) {
                Log.d("result!!!!",result.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    bitmap = withContext(Dispatchers.IO) {
                        downloadProfilePic(result.profilePic)
                    }
                    binding.editProfileImage.setImageBitmap(bitmap)
                    binding.EditProfileContent.setText(result.tags)
                    binding.EditProfileId.setText(result.name)
                }
            } else if(result is UserError) {

            } else {

            }
        }
        binding.btnEditComplete.setOnClickListener {
            val editId = binding.EditProfileId.text.toString()
            //setFragmentResult("request", bundleOf("editId" to editId))
            val editContent = binding.EditProfileContent.text.toString()
            //setFragmentResult("request", bundleOf("editContent" to editContent))
            if (editId.isNotEmpty() && editContent.isNotEmpty()) {
                if(filePath == "") {
                    val uriPathHelper = URIPathHelper()
                    filePath = context?.let { it1 ->
                        getImageUri(it1,bitmap)?.let { it1 -> context?.let { it2 ->
                            uriPathHelper.getPath(
                                it2, it1)
                        } }
                    }.toString()
                    Log.d("zz",filePath)
                }
                Log.d("filePath",filePath)
                CoroutineScope(Dispatchers.Default).launch {
                    Log.d("id", editId)
                    val result = editProfile(
                        UserEditProfileData(
                            editId,
                            File(filePath),
                            editContent,
                            "",
                            "",
                            ""
                        )
                    )
                    if (result is UserEditProfileResponseData) {
                        CoroutineScope(Dispatchers.Main).launch {
                            Log.d("res", "succeesful!")
                            //val direction = ProfileEditFragmentDirections.actionProfileEditFragmentToProfileMyFragment()
                            //findNavController().navigate(direction)
                            mainScreenActivity?.goBack()
                        }
                    } else {

                    }
                }
            }


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
        uri.let { binding.editProfileImage.setImageURI(uri)
            val uriPathHelper = URIPathHelper()
            if (uri != null)
                filePath =
                    this.context?.let { it1 -> uriPathHelper.getPath(it1, uri!!) }.toString()}
    }

    var pictureUri: Uri? = null
    private val getTakePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if(it) {
            pictureUri.let { binding.editProfileImage.setImageURI(pictureUri)
                val uriPathHelper = URIPathHelper()
                if (pictureUri != null)
                    filePath = this.context?.let { it1 -> uriPathHelper.getPath(it1, pictureUri!!) }
                        .toString()}
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
                Toast.makeText(mainScreenActivity, "권한 허용 필요", Toast.LENGTH_SHORT).show()
                mainScreenActivity?.finish()
            }
        }
    }

    private fun createImageFile(): Uri? {
        val now = SimpleDateFormat("yyMMdd_HHmmss").format(Date())
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return mainScreenActivity?.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainScreenActivity = context as MainScreenActivity
    }

    override fun onDestroy() {
        Log.d("프래그먼트 종료", "프래그먼트 종료")
        super.onDestroy()
        mainScreenActivity?.hideNavBar(false)
    }
}