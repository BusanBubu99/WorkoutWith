package com.bubu.workoutwithclient.userinterface

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.ActivityMainBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.EOFException
import java.net.SocketTimeoutException


lateinit var storagePermission: ActivityResultLauncher<String>


class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {

        storagePermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    //setViews()

                } else {
                    Toast.makeText(baseContext, "외부저장소 권한을 승인해야 앱을 사용할 수 있습니다.", Toast.LENGTH_LONG)
                        .show()
                    finish()
                }
            }

        storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        userInformation = UserData
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                var loginObject = UserLoginModule(UserLoginData("ehdrjs6593","gbdngb12@naver.com","1616tkd2"))
                val result = loginObject.getApiData()
                if(result is UserLoginResponseData) {
                    Log.d("result access",result.accessToken)
                    Log.d("result refresh",result.refreshToken)
                } else if (result is UserError) {
                    result.message.forEach{
                        Log.d("result",it)
                    }
                }
                else if(result is SocketTimeoutException) {
                    Log.d("Exception",result.toString())
                } else if(result is EOFException) {
                    Log.d("Exception",result.toString())
                } else if (result is Exception){
                    Log.d("Exception",result.toString())
                }
            }
        }
        binding.button.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                val filepath = "/storage/0B0A-1E06/DCIM/test.png"
                var profileObject = UserEditProfileModule(UserEditProfileData("chong!",filepath,"tags!","busan","jingu","gaegum"))
                Log.d("button","!!")
                val result = profileObject.getApiData()
                if(result is UserEditProfileData) {
                    Log.d("result",result.toString())
                } else if (result is UserError) {
                    result.message.forEach{
                        Log.d("result",it)
                    }
                }
                else if(result is SocketTimeoutException) {

                } else if(result is EOFException) {

                } else if (result is Exception){

                }
            }
        }

        /*CoroutineScope(Dispatchers.Default).launch {
            val password = "1616tkd2"
            val id = "ehdrjs6593"
            val email = "gbdngb12@naver.com"

            val registerObject = UserRegisterModule(UserRegisterData(id,email,password,password))
            val result = registerObject.getApiData()
            if(result is UserLoginResponseData) { //정상
                userInformation.accessToken = result.accessToken
                userInformation.refreshToken = result.refreshToken
                CoroutineScope(Dispatchers.Main).launch {
                    //UI
                }
            } else if (result is UserError) {
                result.message.forEach {
                    Log.d("error",it)
                }
            } else if (result is SocketTimeoutException) {

            } else if (result is EOFException) {

            } else if (result is Exception) {

            }

        }*/



    }
}