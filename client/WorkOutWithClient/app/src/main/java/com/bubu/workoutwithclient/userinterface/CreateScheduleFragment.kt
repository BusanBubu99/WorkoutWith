package com.bubu.workoutwithclient.userinterface

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bubu.workoutwithclient.databinding.FragmentCreateScheduleBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class CreateScheduleFragment : Fragment() {

    lateinit var matchRoomActivity: MatchRoomActivity
    lateinit var binding: FragmentCreateScheduleBinding
    lateinit var startTime: String
    lateinit var endTime: String
    lateinit var date: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreateScheduleBinding.inflate(inflater, container, false)
        //var startTime = "09:21:33.1234"
        //val endTime = "23:00:00.1234"
        //val date = "2022-12-13"

        binding.dateButton.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    date = "${year}-${month + 1}-${dayOfMonth}"
                    binding.dateTextView.text = "${year}년 ${month + 1}월 ${dayOfMonth}일"//"날짜 : " + date
                }
            DatePickerDialog(
                matchRoomActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.startTimeButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                startTime = "${hourOfDay}:${minute}:00.0000"
                binding.startTimeView.text = "${hourOfDay}시 ${minute}분"//"시간 : " + startTime
            }
            TimePickerDialog(
                matchRoomActivity,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.endTimeButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                endTime = "${hourOfDay}:${minute}:00.0000"
                binding.endTimeView.text = "${hourOfDay}시 ${minute}분"//"시간 : " + endTime
            }
            TimePickerDialog(
                matchRoomActivity,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.btnSubmitSchedule.setOnClickListener {
            Log.d("re","click")
            Log.d("Title",binding.editTitle.text.toString())
            Log.d("Title",binding.dateTextView.text.toString())
            Log.d("Title",binding.startTimeView.text.toString())
            Log.d("Title",binding.endTimeView.text.toString())
            Log.d("Title",binding.editContent.text.toString())

            if (binding.editTitle.text.toString().isNotEmpty() && binding.dateTextView.text.toString().isNotEmpty()
                && binding.startTimeView.text.toString().isNotEmpty()
                && binding.endTimeView.text.toString().isNotEmpty() && binding.editContent.text.toString().isNotEmpty()
            ) {
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
                    if (result is UserCreateRoomVoteResponseData) {
                        createVote(
                            userInformation.userId,
                            matchId,
                            binding.editTitle.text.toString(),
                            result.voteId,
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
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        matchRoomActivity = context as MatchRoomActivity
    }

}