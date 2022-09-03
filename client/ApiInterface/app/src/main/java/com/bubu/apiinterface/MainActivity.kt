package com.bubu.apiinterface

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bubu.apiinterface.databinding.ActivityMainBinding
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.EOFException
import java.net.SocketTimeoutException


lateinit var userInformation : UserData

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            /**
             * How to Use in Coroutine
             *
             * request
             * id : 사용자 id
             * pw : 사용자 비밀번호
             *
             * response
             * [http보류]
             * token : 사용자 인증정보
             * */

            userInformation = UserData
            //userInformation.token = "test" //init user token
            CoroutineScope(Dispatchers.Default).launch {
                var searchUserData = JsonObject()
                searchUserData.addProperty("username", binding.username.text.toString())
                searchUserData.addProperty("email",binding.email.text.toString())
                searchUserData.addProperty("password1",binding.password.text.toString())
                searchUserData.addProperty("password2",binding.password.text.toString())
                val testObject = UserRegisterModule(searchUserData)
                when (val result = testObject.getApiData()) {
                    is JsonObject -> { // Successful
                        Log.d("result!", result.toString())
                        //CoroutineScope(Dispatchers.Main).launch {
                        //  Staring the UI
                        //}
                    }
                    is UserError -> { //if Error
                        Log.d("result", "I AM HERE!")
                        result.message.forEach { //Print Error Message
                            Log.d("result Error",it)
                        }
                    }
                    is SocketTimeoutException -> {
                        Log.d("result Exception",result.toString())
                    }
                    is EOFException -> {
                        Log.d("result Exception",result.toString())
                    }
                    is Exception -> {
                        Log.d("result Exception",result.toString())
                    }
                }
            }

        }

    }
}