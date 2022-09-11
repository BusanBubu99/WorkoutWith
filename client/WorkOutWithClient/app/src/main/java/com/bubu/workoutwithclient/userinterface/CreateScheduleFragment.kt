package com.bubu.workoutwithclient.userinterface

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bubu.workoutwithclient.databinding.FragmentCreateScheduleBinding

class CreateScheduleFragment : Fragment() {

    lateinit var majorScreen: MajorScreen
    lateinit var binding: FragmentCreateScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateScheduleBinding.inflate(inflater, container, false)
        binding.btnSubmitSchedule.setOnClickListener {
            val direction = CreateScheduleFragmentDirections.actionCreateScheduleFragmentToMatchingTeamFragment()
            findNavController().navigate(direction)
        }
        return binding.root

    }

}