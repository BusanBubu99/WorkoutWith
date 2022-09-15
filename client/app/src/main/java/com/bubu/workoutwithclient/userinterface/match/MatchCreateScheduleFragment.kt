package com.bubu.workoutwithclient.userinterface.match

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bubu.workoutwithclient.databinding.MatchCreateScheduleFragmentBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import com.bubu.workoutwithclient.userinterface.gameCodeList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class MatchCreateScheduleFragment : Fragment() {

    lateinit var matchRoomActivity: MatchRoomActivity
    lateinit var binding: MatchCreateScheduleFragmentBinding
    lateinit var startTime: String
    lateinit var endTime: String
    lateinit var date: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = MatchCreateScheduleFragmentBinding.inflate(inflater, container, false)

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
            Log.d("Title",binding.editScheduleTitle.text.toString())
            Log.d("Title",binding.dateTextView.text.toString())
            Log.d("Title",binding.startTimeView.text.toString())
            Log.d("Title",binding.endTimeView.text.toString())
            Log.d("Title",binding.editScheduleContent.text.toString())

            if (binding.editScheduleTitle.text.toString().isNotEmpty() && binding.dateTextView.text.toString().isNotEmpty()
                && binding.startTimeView.text.toString().isNotEmpty()
                && binding.endTimeView.text.toString().isNotEmpty() && binding.editScheduleContent.text.toString().isNotEmpty()
            ) {
                Log.d("value!!", arguments?.getString("matchId").toString())
                CoroutineScope(Dispatchers.Default).launch {
                    val matchId = arguments?.getString("matchId").toString()
                    val voteObject = UserCreateRoomVoteModule(
                        UserCreateRoomVoteData(
                            binding.editScheduleTitle.text.toString(),
                            matchId,
                            startTime,
                            endTime,
                            date,
                            binding.editScheduleContent.text.toString()
                        )
                    )
                    val result = voteObject.getApiData()
                    if (result is UserCreateRoomVoteResponseData) {
                        createVote(
                            userInformation.userId,
                            matchId,
                            binding.editScheduleTitle.text.toString(),
                            result.voteId,
                            startTime,
                            endTime,
                            date,
                            binding.editScheduleContent.text.toString()
                        )
                        CoroutineScope(Dispatchers.Main).launch {
                            matchRoomActivity?.goBack()
                        }
                    } else if (result is UserError) {

                    } else {

                    }
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "모든 내용을 적어주세요!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        matchRoomActivity = context as MatchRoomActivity
    }

    override fun onResume() {
        matchRoomActivity?.setTitle("일정 생성하기")
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("dest","dest")
        matchRoomActivity?.setTitle("${matchRoomData.county} ${matchRoomData.district} ${gameCodeList[matchRoomData.game.toInt()]} 매칭방")
    }

}