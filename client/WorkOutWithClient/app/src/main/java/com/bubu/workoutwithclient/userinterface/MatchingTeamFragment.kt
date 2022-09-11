package com.bubu.workoutwithclient.userinterface

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bubu.workoutwithclient.databinding.FragmentMatchingTeamBinding

/*class MatchingTeamFragment : Fragment() {
    private lateinit var callback: OnBackPressedCallback

    lateinit var majorScreen: MajorScreen
    lateinit var binding : FragmentMatchingTeamBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMatchingTeamBinding.inflate(inflater, container, false)
        val data : MutableList<MatchingTeam> = loadMatchingTeamData()
        var adapter = MatchingTeamAdapter()
        adapter.listMatchingTeamData = data
        with(binding) {
            recyclerProfile.adapter = adapter
            recyclerProfile.layoutManager = GridLayoutManager(majorScreen, 5)

            btnCreateSchedule.setOnClickListener {
                val direction = MatchingTeamFragmentDirections.actionMatchingTeamFragmentToCreateScheduleFragment()
                findNavController().navigate(direction)
            }
            btnJoinSchedule.setOnClickListener {
                val direction = MatchingTeamFragmentDirections.actionMatchingTeamFragmentToJoinScheduleFragment()
                findNavController().navigate(direction)
            }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("request") { key, bundle ->
            bundle.getString("matchId")?.let {
                Log.d("결과값", "${it}")
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val direction = MatchingTeamFragmentDirections.actionMatchingTeamFragmentToHomeFragment()
                findNavController().navigate(direction)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        majorScreen = context as MajorScreen
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    fun loadMatchingTeamData() : MutableList<MatchingTeam> {
        val data : MutableList<MatchingTeam> = mutableListOf()
        
        //for(no in 1..30) {
        //    val title = "팀원 ${no}"
        //    var matchingTeam = MatchingTeam(title)
        //    data.add(matchingTeam)
        //}
        return data
    }
}*/