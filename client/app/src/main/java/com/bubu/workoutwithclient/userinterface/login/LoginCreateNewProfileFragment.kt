package com.bubu.workoutwithclient.userinterface.login

import android.Manifest
import android.app.AlertDialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
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
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.LoginCreateNewProfileFragmentBinding
import com.bubu.workoutwithclient.retrofitinterface.UserEditProfileData
import com.bubu.workoutwithclient.retrofitinterface.UserEditProfileModule
import com.bubu.workoutwithclient.retrofitinterface.UserEditProfileResponseData
import com.bubu.workoutwithclient.retrofitinterface.UserError
import com.bubu.workoutwithclient.userinterface.MainScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.EOFException
import java.io.File
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*

class URIPathHelper {
    fun getPath(context: Context, uri: Uri): String? {
        val isKitKatorAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKatorAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.getContentResolver()
                .query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}


suspend fun editProfile(data: UserEditProfileData): Any? {
    val editProfileObject = UserEditProfileModule(data)
    val result = editProfileObject.getApiData()
    if (result is UserEditProfileResponseData) {
        return result
    } else if (result is UserError) {
        result.message.forEach {
            Log.d("userError", it)
        }
        return result
    } else if (result is SocketTimeoutException) {
        return result
    } else if (result is EOFException) {
        return result
    } else if (result is Exception) {
        return result
    } else {
        return result
    }
}


class LoginCreateNewProfileFragment : Fragment() {

    lateinit var binding: LoginCreateNewProfileFragmentBinding
    lateinit var mainActivity: LoginActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginCreateNewProfileFragmentBinding.inflate(inflater, container, false)
        requestMultiplePermission.launch(permissionList)
        binding.editNewProfileImage.setOnClickListener {
            openDialog(mainActivity)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = Intent(activity, MainScreenActivity::class.java)
        binding.btnNewEditComplete.setOnClickListener {
            val editId = binding.EditNewProfileId.text.toString()
            //setFragmentResult("request", bundleOf("editId" to editId))
            val editContent = binding.EditNewProfileContent.text.toString()
            //setFragmentResult("request", bundleOf("editContent" to editContent))
            if (filePath != null && editId.isNotEmpty() && editContent.isNotEmpty()) {
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
                            mainActivity?.startActivity(intent)
                            mainActivity?.finish()
                        }
                    } else {

                    }
                }
            }
        }
        setFragmentResultListener("request") { key, bundle ->
            bundle.getString("profileId")?.let {
                binding.EditNewProfileId.setText(it)
            }
            bundle.getString("profileContent")?.let {
                binding.EditNewProfileContent.setText(it)
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
            pictureUri = createImageFile()
            getTakePicture.launch(pictureUri)
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
                binding.editNewProfileImage.setImageURI(uri)

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
                binding.editNewProfileImage.setImageURI(pictureUri)
                val uriPathHelper = URIPathHelper()
                if (pictureUri != null)
                    filePath = this.context?.let { it1 -> uriPathHelper.getPath(it1, pictureUri!!) }
                        .toString()
            }
        }
    }

    private val getTakePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap.let { binding.editNewProfileImage.setImageBitmap(bitmap) }
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
                    Toast.makeText(mainActivity, "권한 허용 필요", Toast.LENGTH_SHORT).show()
                    mainActivity?.finish()
                }
            }
        }

    private fun createImageFile(): Uri? {
        val now = SimpleDateFormat("yyMMdd_HHmmss").format(Date())
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return mainActivity?.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            content
        )
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as LoginActivity
    }

    override fun onResume() {
        mainActivity?.setTitle("새로운 프로필 등록")
        super.onResume()
    }
}