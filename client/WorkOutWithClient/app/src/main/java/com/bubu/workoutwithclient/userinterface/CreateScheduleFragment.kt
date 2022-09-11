package com.bubu.workoutwithclient.userinterface

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bubu.workoutwithclient.databinding.FragmentCreateScheduleBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreateScheduleFragment : Fragment() {

    lateinit var matchRoomActivity: MatchRoomActivity
    lateinit var binding: FragmentCreateScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateScheduleBinding.inflate(inflater, container, false)
        val startTime = "09:21:33.1234"
        val endTime = "23:00:00.1234"
        val date = "2022-12-13"
        binding.btnSubmitSchedule.setOnClickListener {
            Log.d("value!!", arguments?.getString("matchId").toString())
            CoroutineScope(Dispatchers.Default).launch {
                val matchId = arguments?.getString("matchId").toString()
                val voteObject = UserCreateRoomVoteModule(
                    UserCreateRoomVoteData(
                        binding.editTitle.text.toString(),
                        matchId,
                        startTime,
                        endTime,
                        date,
                        binding.editContent.text.toString()
                    )
                )
                val result = voteObject.getApiData()
                if (result in 200..299) {
                    startVote(
                        userInformation.userId,
                        matchId,
                        binding.editTitle.text.toString(),
                        "testVoteId",
                        startTime,
                        endTime,
                        date,
                        binding.editContent.text.toString()
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        matchRoomActivity?.goBack()
                    }
                } else if (result is UserError) {

                } else {

                }
            }
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        matchRoomActivity = context as MatchRoomActivity
    }

}