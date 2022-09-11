package com.bubu.workoutwithclient.userinterface

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bubu.workoutwithclient.databinding.FragmentJoinScheduleBinding


class JoinScheduleFragment : Fragment() {

    lateinit var majorScreen: MajorScreen
    lateinit var binding : FragmentJoinScheduleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinScheduleBinding.inflate(inflater, container, false)
        val data : MutableList<MatchingTeam> = loadJoinedTeamData()
        var adapter = MatchingTeamAdapter()
        adapter.listMatchingTeamData = data
        val builder = AlertDialog.Builder(majorScreen)
        builder.setMessage("참가 신청이 완료되었습니다!")
        with(binding) {
            recyclerJoinedProfile.adapter = adapter
            recyclerJoinedProfile.layoutManager = GridLayoutManager(majorScreen, 5)

            btnSubmitJoinSchedule.setOnClickListener {
                val direction = JoinScheduleFragmentDirections.actionJoinScheduleFragmentToMatchingTeamFragment()
                builder.setPositiveButton("확인", DialogInterface.OnClickListener{dialogInterface, i -> findNavController().navigate(direction) })
                builder.show()
            }
        }
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        majorScreen = context as MajorScreen
    }

    fun loadJoinedTeamData() : MutableList<MatchingTeam> {

        val data : MutableList<MatchingTeam> = mutableListOf()
        for(no in 1..30) {
            val title = "팀원 ${no}"
            var matchingTeam = MatchingTeam(title)
            data.add(matchingTeam)
        }
        return data
    }

}