package com.bubu.apiinterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.bubu.apiinterface.databinding.ActivityMainBinding
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
            userInformation.token = "test" //init user token
            CoroutineScope(Dispatchers.Default).launch {
                var searchUserData = JsonObject()
                searchUserData.addProperty("target","dong")
                searchUserData.addProperty("count",3)
                val testObject = UserTestModule(searchUserData)
                val result = testObject.getApiData()
                if(result == null) {
                    Log.d("Error!", "Please Check the Log")
                } else { //Success!
                    Log.d("result",result.toString())
                    Log.d("result test", result[0].id)
                    Log.d("result test", result[0].name)
                    Log.d("result test", result[0].tags)
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.textView.text = result[0].name
                    }
                }
            }

        }

    }
}