package com.bubu.apiinterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
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
        CoroutineScope(Dispatchers.Default).launch {
            var loginData = JsonObject()
            loginData.addProperty("id","dong")
            loginData.addProperty("pw","password")
            val testObject = UserLoginModule(loginData,"")
            val result = testObject.getApiData()
            if(result == null) {
                Log.d("Error!", "Please Check the Log")
            } else { //Success!
                Log.d("result",result.token)
            }
        }


    }
}