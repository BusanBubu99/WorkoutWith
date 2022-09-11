package com.bubu.workoutwithclient.userinterface

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bubu.workoutwithclient.databinding.FragmentLoginBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.EOFException
import java.lang.Exception
import java.net.SocketTimeoutException

suspend fun login(data: UserLoginData): Any? {
    val loginObject = UserLoginModule(data)
    val result = loginObject.getApiData()
    if (result is UserLoginResponseData) {
        return result
    } else if (result is UserError) {
        result.message.forEach {
            Log.d("UserError", it)
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

suspend fun isProfile() : Any? {
    val isProfileObject = UserIsProfileModule()
    val result = isProfileObject.getApiData()
    if(result is UserIsProfileResponseData){
        return result
    }  else if (result is UserError) {
        result.message.forEach {
            Log.d("UserError", it)
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

class LoginFragment : Fragment() {
    var mainActivity: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val intent = Intent(activity, MajorScreen::class.java)
        val binding = FragmentLoginBinding.inflate(inflater, container, false)


        with(binding) {
            btnRegister.setOnClickListener { mainActivity?.goRegisterFragment() }
            btnFindId.setOnClickListener { mainActivity?.goFindIdFragment() }
            btnFindPassword.setOnClickListener { mainActivity?.goFindPasswordFragment() }
            btnLogin.setOnClickListener {
                CoroutineScope(Dispatchers.Default).launch {
                    val result = login(UserLoginData(binding.editId.text.toString().lowercase(), binding.editId.text.toString().lowercase(),
                        binding.editPassword.text.toString()))
                    if(result is UserLoginResponseData){
                        Log.d("Success",result.toString())
                        val res = isProfile()
                        if(res is UserIsProfileResponseData) {
                            if(res.snsResult == 0) {
                                mainActivity?.goNewProfileFragment()
                            } else if(res.snsResult == 99){
                                CoroutineScope(Dispatchers.Main).launch {
                                    val builder = AlertDialog.Builder(mainActivity)
                                    builder.setMessage("Test")
                                    builder.setPositiveButton("확인", DialogInterface.OnClickListener{
                                            dialogInterface, i -> mainActivity?.startActivity(intent) })
                                    builder.show()
                                }
                            }
                        } else {

                        }
                    } else {

                    }
                }


            }
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
}