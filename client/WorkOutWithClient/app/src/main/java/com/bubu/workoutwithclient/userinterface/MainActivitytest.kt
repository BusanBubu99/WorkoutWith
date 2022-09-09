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
import java.io.File
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.math.log


lateinit var storagePermission: ActivityResultLauncher<String>

fun register() {
    CoroutineScope(Dispatchers.Default).launch {
        val registerObject = UserRegisterModule(
            UserRegisterData(
                "gbdngb12",
                "gbdngb12@naver.com",
                "1616tkd2",
                "1616tkd2"
            )
        )
        val result = registerObject.getApiData()
        if (result in 200..299) {
            Log.d("successful!", "Check the Email!")
        } else if (result is UserError) {
            result.message.forEach {
                Log.d("result Error", it)
            }
        } else if (result is SocketTimeoutException) {

        } else if (result is EOFException) {

        } else if (result is Exception) {

        }
    }
}

fun login() {
    CoroutineScope(Dispatchers.Default).launch {
        val loginObject =
            UserLoginModule(UserLoginData("gbdngb12", "gbdngb12@Naver.com", "1616tkd2"))
        val result = loginObject.getApiData()
        if (result is UserLoginResponseData) {
            Log.d("successful!", userInformation.accessToken)
            Log.d("successful!", userInformation.refreshToken)
            Log.d("successful!", userInformation.userId)
        } else if (result is UserError) {
            result.message.forEach {
                Log.d("result Error", it)
            }
        } else if (result is SocketTimeoutException) {

        } else if (result is EOFException) {

        } else if (result is Exception) {

        }
    }
}

fun isProfile() {
    CoroutineScope(Dispatchers.Default).launch {
        val isProfileObject = UserIsProfileModule()
        val result = isProfileObject.getApiData()
        if (result is UserIsProfileResponseData) {
            Log.d("successful!", result.snsResult)
        } else if (result is UserError) {
            result.message.forEach {
                Log.d("result Error", it)
            }
        } else if (result is SocketTimeoutException) {

        } else if (result is EOFException) {

        } else if (result is Exception) {

        }
    }
}

fun logout() {
    CoroutineScope(Dispatchers.Default).launch {
        val logoutObject = UserLogOutModule()
        val result = logoutObject.getApiData()
        if (result in 200..299) {
            Log.d("successful!", result.toString())
        } else if (result is UserError) {
            result.message.forEach {
                Log.d("result Error", it)
            }
        } else if (result is SocketTimeoutException) {

        } else if (result is EOFException) {

        } else if (result is Exception) {

        }
    }
}

fun changePassword() {
    CoroutineScope(Dispatchers.Default).launch {
        val logoutObject =
            UserChangePasswordModule(UserChangePasswordData("1616tkd2@", "1616tkd2@"))
        val result = logoutObject.getApiData()
        if (result in 200..299) {
            Log.d("successful!", result.toString())
        } else if (result is UserError) {
            result.message.forEach {
                Log.d("result Error", it)
            }
        } else if (result is SocketTimeoutException) {

        } else if (result is EOFException) {

        } else if (result is Exception) {

        }
    }
}

fun editProfile() {
    CoroutineScope(Dispatchers.Default).launch {
        val editProfile = UserEditProfileModule(
            UserEditProfileData(
                "동건",
                File("/storage/0B0A-1E06/Download/test.png"), "tags!", "부산", "진구", "개금"
            )
        )
        val result = editProfile.getApiData()
        if (result is UserEditProfileResponseData) {
            Log.d("successful!", result.toString())
        } else if (result is UserError) {
            result.message.forEach {
                Log.d("result Error", it)
            }
        } else if (result is SocketTimeoutException) {

        } else if (result is EOFException) {

        } else if (result is Exception) {

        }
    }
}

fun getProfile() {
    CoroutineScope(Dispatchers.Default).launch {
        val getProfileObject = UserGetProfileModule(UserGetProfileData("ehdrjs7789"))
        val result = getProfileObject.getApiData()
        if (result is UserGetProfileResponseData) {
            Log.d("successful!", result.toString())
        } else if (result is UserError) {
            result.message.forEach {
                Log.d("result Error", it)
            }
        } else if (result is SocketTimeoutException) {

        } else if (result is EOFException) {

        } else if (result is Exception) {

        }
    }
}

fun startMatch() {
    CoroutineScope(Dispatchers.Default).launch {
        val startMatchObject = UserStartMatchModule(UserStartMatchData("부산", "진구", "개금", "20"))
        val result = startMatchObject.getApiData()
        if (result is UserStartMatchResponseData) {
            Log.d("successful!", result.toString())
        } else if (result is UserError) {
            result.message.forEach {
                Log.d("result Error", it)
            }
        } else if (result is SocketTimeoutException) {

        } else if (result is EOFException) {

        } else if (result is Exception) {

        }
    }
}

fun createCommunity() {
    CoroutineScope(Dispatchers.Default).launch {
        val createCommunityObject = UserCreateCommunityModule(
            UserCreateCommunityData(
                "title!",
                File("/storage/0B0A-1E06/Download/test.png"),
                "Test String!"
            )
        )
        val result = createCommunityObject.getApiData()
        if (result is UserStartMatchResponseData) {
            Log.d("successful!", result.toString())
        } else if (result is UserError) {
            result.message.forEach {
                Log.d("result Error", it)
            }
        } else if (result is SocketTimeoutException) {

        } else if (result is EOFException) {

        } else if (result is Exception) {

        }
    }
}

fun getAddress() {
    CoroutineScope(Dispatchers.Default).launch {
        val addressObject = UserGetAddressModule()
        val result = addressObject.getApiData()
        if (result is List<*>) {
            result as List<UserCityResponseData>
            result.forEach {
                Log.d("cityName", it.cityName)
                Log.d("cityCode", it.cityCode)
            }
            val result2 = addressObject.getDetailCity(result[1].cityCode)
            if (result2 is List<*>) {
                result2 as List<UserCityResponseData>
                result2.forEach {
                    Log.d("name", it.cityName)
                    Log.d("code", it.cityCode)
                }
                val result3 = addressObject.getDetailCity(result2[6].cityCode)
                if (result3 is List<*>) {
                    result3 as List<UserCityResponseData>
                    result3.forEach {
                        Log.d("name",it.cityName)
                        Log.d("code",it.cityCode)
                    }
                } else if (result3 is UserError) {
                    result3.message.forEach {
                        Log.d("result Error", it)
                    }
                } else if (result3 is SocketTimeoutException) {

                } else if (result3 is EOFException) {

                } else if (result3 is Exception) {

                }
            } else if (result2 is UserError) {
                result2.message.forEach {
                    Log.d("result Error", it)
                }
            } else if (result2 is SocketTimeoutException) {

            } else if (result2 is EOFException) {

            } else if (result2 is Exception) {

            }
        } else if (result is UserError) {
            result.message.forEach {
                Log.d("result Error", it)
            }
        } else if (result is SocketTimeoutException) {

        } else if (result is EOFException) {

        } else if (result is Exception) {

        }

    }
}


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
        binding.loginButton.setOnClickListener {
            login()
        }

        binding.testButton.setOnClickListener {
            getAddress()
        }
        binding.register.setOnClickListener {
            register()
        }


        /*binding.login.setOnClickListener {
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
                val profilePic = File(filepath)
                var profileObject = UserEditProfileModule(UserEditProfileData("chong!",profilePic,"tags!","busan","jingu","gaegum"))
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

        CoroutineScope(Dispatchers.Default).launch {
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