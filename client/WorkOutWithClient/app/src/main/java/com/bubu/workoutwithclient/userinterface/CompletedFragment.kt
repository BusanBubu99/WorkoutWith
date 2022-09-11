package com.bubu.workoutwithclient.userinterface

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.FragmentCompletedBinding

class CompletedFragment : Fragment() {

    lateinit var majorScreen: MajorScreen
    lateinit var binding: FragmentCompletedBinding
    var state: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompletedBinding.inflate(inflater, container, false)
        binding.ChoiceImage.setOnClickListener {
            if (state == true) {
                binding.ChoiceImage.setImageResource(R.drawable.image_good)
                state = false
            } else {
                binding.ChoiceImage.setImageResource(R.drawable.image_good_choiced)
                state = true
            }
        }
        return binding.root
    }
}