package com.bubu.workoutwithclient.userinterface

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bubu.workoutwithclient.databinding.FragmentHomeBinding
import com.bubu.workoutwithclient.databinding.RecyclerHomeBinding
import com.bubu.workoutwithclient.retrofitinterface.UserError
import com.bubu.workoutwithclient.retrofitinterface.UserGetMatchListModule
import com.bubu.workoutwithclient.retrofitinterface.UserGetMatchListResponseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.EOFException
import java.lang.Exception
import java.net.SocketTimeoutException


suspend fun getMatchList(): Any? {
    val getMatchListObject = UserGetMatchListModule()
    val result = getMatchListObject.getApiData()
    if (result is List<*>) {
        if (result.isEmpty()) {
            return 0
        }
        return result as List<UserGetMatchListResponseData>
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

class HomeFragment : Fragment() {
    lateinit var majorScreen: MajorScreen
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        CoroutineScope(Dispatchers.Default).launch {
            val result = getMatchList()
            //Log.d("result!",result.toString())
            if(result is List<*>) {
                CoroutineScope(Dispatchers.Main).launch {
                    var adapter = HomeAdapter(majorScreen)
                    adapter.userMatchList = result as MutableList<UserGetMatchListResponseData>
                    binding.RecyclerHome.adapter = adapter
                    binding.RecyclerHome.layoutManager = LinearLayoutManager(majorScreen)
                }
            } else if(result == 0) {
                Log.d("result","아직 매칭정보가 없습니다.")
            } else {
            }
        }

        binding.btnGoMatching.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToMatchingStartFragment()
            findNavController().navigate(direction)
        }
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        majorScreen = context as MajorScreen
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}