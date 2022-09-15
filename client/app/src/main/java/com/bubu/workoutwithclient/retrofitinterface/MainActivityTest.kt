package com.bubu.workoutwithclient.retrofitinterface

/*/mport android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bubu.apiinterface.databinding.ActivityMainBinding
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.EOFException
import java.net.SocketTimeoutException*/




/*class ImageTestModule(override val userData: Any?) : UserApiInterface {

    interface ImageTestInterface {
        @GET("/v1/profile")
        fun get(
            @Query("token") token: String
        ): Call<Any>
        //보내는 데이터 형식
    }
    override suspend fun getApiData(): Any? {
        TODO("Not yet implemented")
    }
}

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        userInformation = UserData



        /**
         * get Address Module Usage
         *
        /*CoroutineScope(Dispatchers.Default).launch {
            var testObject = UserGetAddressModule(null)
            val result = testObject.getApiData()
            if(result is List<*>) {
                result.forEach {
                    it as JsonObject
                    Log.d("name",it.get("name").toString())
                    Log.d("code",it.get("cd").toString())
                }
            } else if(result is UserError) {
                //..
            } else if(result is Exception) {
                //..
            }

            val result2 = testObject.getDetailCity("11")
            if(result2 is List<*>) {
                result2.forEach {
                    it as JsonObject
                    Log.d("name",it.get("name").toString())
                    Log.d("code",it.get("cd").toString())
                }
            } else if(result2 is UserError) {
                //..
            } else if(result2 is Exception) {
                //..
            }
            val result3 = testObject.getDetailCity("11020")
            if(result3 is List<*>) {
                result3.forEach {
                    it as JsonObject
                    Log.d("name",it.get("name").toString())
                    Log.d("code",it.get("cd").toString())
                }
            } else if(result3 is UserError) {
                //..
            } else if(result3 is Exception) {
                //..
            }
        }










        /
         * Any operation that requires authentication must run this procedure
         /
        binding.checkButton.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                var auth = UserAuthModule(null)
                when (auth.getApiData()) {
                    true -> {
                        //Do Any Operation or Jobs..
                    }
                    is UserError -> {
                        //Error Handling
                    }
                    is UninitializedPropertyAccessException -> {
                        //Error Handling
                    }
                    is SocketTimeoutException -> {
                        //Error Handling
                    }
                    is EOFException -> {
                        //Error Handling
                    }
                    is Exception -> {
                        //Error Handling
                    }
                }
            }
        }



        binding.button.setOnClickListener {
            /
             * {"username":"gbdngb12", "email":"gbdngb12@Naver.com", "password":"1919tkd2@"}
             *
             * UserLogin Usage
             /
            CoroutineScope(Dispatchers.Default).launch {
                val testObject = UserLoginModule()
                when (val result = testObject.getApiData()) {
                    is UserLoginResponseData -> { // Successful
                        Log.d("result!", result.toString())
                        CoroutineScope(Dispatchers.Main).launch {
                            //  Staring the UI
                            binding.accessToken.text = result.accessToken
                            binding.refreshToken.text = result.refreshToken
                        }
                    }
                    is UserError -> { //if Error
                        Log.d("result", "I AM HERE!")
                        result.message.forEach { //Print Error Message
                            Log.d("result Error", it)
                        }
                    }
                    is SocketTimeoutException -> {
                        Log.d("result Exception", result.toString())
                    }
                    is EOFException -> {
                        Log.d("result Exception", result.toString())
                    }
                    is Exception -> {
                        Log.d("result Exception", result.toString())
                    }
                }
            }

        }

    }
}*/

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
 */