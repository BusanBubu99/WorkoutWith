package com.bubu.workoutwithclient.userinterface

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bubu.workoutwithclient.databinding.ActivityUserProfileBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserProfileActivity : AppCompatActivity() {

    val binding by lazy { ActivityUserProfileBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val intent = intent
        //Log.d("Activity Intent",intent.getStringExtra("targetId").toString())
        var targetId = intent.getStringExtra("targetId").toString()
        CoroutineScope(Dispatchers.Default).launch {
            val getProfileObject = UserGetProfileModule(UserGetProfileData(targetId))
            val result = getProfileObject.getApiData()
            if(result is UserGetProfileResponseData) {
                Log.d("result!!!!",result.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = withContext(Dispatchers.IO) {
                        downloadProfilePic(result.profilePic)
                    }
                    binding.profileImage.setImageBitmap(bitmap)
                    //if(userInformation.userId != result.userId)
                    //    binding.btnEditProfile.isVisible = false
                    binding.textProfileContent.text = result.tags
                    binding.textProfileId.text = result.name
                }
            } else if(result is UserError) {

            } else {

            }
        }

    }
}