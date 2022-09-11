package com.bubu.workoutwithclient.userinterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bubu.workoutwithclient.databinding.ActivityUserProfileBinding
import com.bubu.workoutwithclient.retrofitinterface.UserGetProfileData
import com.bubu.workoutwithclient.retrofitinterface.UserGetProfileModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        }

    }
}