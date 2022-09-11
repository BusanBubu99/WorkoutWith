package com.bubu.workoutwithclient.userinterface

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bubu.workoutwithclient.databinding.FragmentRegisterBinding
import com.bubu.workoutwithclient.retrofitinterface.UserError
import com.bubu.workoutwithclient.retrofitinterface.UserRegisterData
import com.bubu.workoutwithclient.retrofitinterface.UserRegisterModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.EOFException
import java.lang.Exception
import java.net.SocketTimeoutException

suspend fun register(username: String, email: String, password1: String, password2: String): Any? {
    val registerObject = UserRegisterModule(UserRegisterData(username, email, password1, password2))
    val result = registerObject.getApiData()
    if (result in 200..299) {
        return true
    } else if (result is UserError) {
        result.message.forEach {
            Log.d("usererror", it)
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

fun convertToLower(data : String){

}

class RegisterFragment : Fragment() {
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.btnRegisterConfirm.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                val result = register(
                    binding.editRegisterEmailAddress.text.toString().lowercase(),
                    binding.editRegisterEmailAddress.text.toString().lowercase(),
                    binding.editRegisterPassword.text.toString(),
                    binding.editRegisterPasswordConfirm.text.toString()
                )
				if(result == true) {
                    CoroutineScope(Dispatchers.Main).launch {
                        mainActivity.goBack()
                    }
				} else {
					Log.d("error",result.toString())
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