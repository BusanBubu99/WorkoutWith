package com.bubu.workoutwithclient.userinterface.match

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.MatchHomeFragmentBinding
import com.bubu.workoutwithclient.retrofitinterface.UserError
import com.bubu.workoutwithclient.retrofitinterface.UserGetMatchListModule
import com.bubu.workoutwithclient.retrofitinterface.UserGetMatchListResponseData
import com.bubu.workoutwithclient.userinterface.MainScreenActivity
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

class MatchHomeFragment : Fragment() {
    lateinit var mainScreenActivity: MainScreenActivity
    lateinit var binding: MatchHomeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MatchHomeFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        CoroutineScope(Dispatchers.Default).launch {
            val result = getMatchList()
            //Log.d("result!",result.toString())
            CoroutineScope(Dispatchers.Main).launch {
                if (result is List<*>) {
                    var adapter = MatchHomeAdapter(mainScreenActivity)
                    adapter.userMatchList = result as MutableList<UserGetMatchListResponseData>
                    binding.RecyclerHome.adapter = adapter
                    binding.RecyclerHome.layoutManager = LinearLayoutManager(mainScreenActivity)

                } else if (result == 0) {
                    Log.d("result", "아직 매칭정보가 없습니다.")
                    binding.btnNewMatchStart.isVisible = true
                    binding.textNewNotice.isVisible = true
                } else {
                }
            }
        }

        binding.btnNewMatchStart.setOnClickListener {
            val direction =
                MatchHomeFragmentDirections.actionMatchHomeFragmentToMatchStartFragment()
            findNavController().navigate(direction)
        }
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainScreenActivity = context as MainScreenActivity
    }

    override fun onResume() {
        mainScreenActivity?.setTitle("내 매칭 리스트")
        mainScreenActivity?.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.common_action_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_common_add_start) {
            val direction =
                MatchHomeFragmentDirections.actionMatchHomeFragmentToMatchStartFragment()
            findNavController().navigate(direction)
        }
        return super.onOptionsItemSelected(item)
    }

}