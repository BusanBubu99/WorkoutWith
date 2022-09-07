package com.bubu.apiinterface

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


lateinit var userInformation: UserData

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