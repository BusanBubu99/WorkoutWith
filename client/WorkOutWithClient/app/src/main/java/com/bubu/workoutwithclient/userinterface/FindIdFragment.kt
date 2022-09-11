package com.bubu.workoutwithclient.userinterface

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bubu.workoutwithclient.databinding.FragmentFindIdBinding

class FindIdFragment : Fragment() {

	lateinit var mainActivity: MainActivity
	lateinit var binding: FragmentFindIdBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?

	): View? {
		binding = FragmentFindIdBinding.inflate(inflater, container, false)
		binding.btnBackFromFindId.setOnClickListener { mainActivity?.goBack() }
		return binding.root
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		mainActivity = context as MainActivity
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.btnFindIdSendCode.setOnClickListener {
			binding.editFindIdCode.isVisible = true
			binding.btnFindIdCodeConfirm.isVisible = true
		}

		binding.btnFindIdCodeConfirm.setOnClickListener {
			binding.textFindIdShow.isVisible = true
		}
	}
}