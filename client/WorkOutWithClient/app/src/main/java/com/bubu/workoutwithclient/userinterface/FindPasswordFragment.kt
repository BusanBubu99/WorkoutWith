package com.bubu.workoutwithclient.userinterface

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bubu.workoutwithclient.databinding.FragmentFindPasswordBinding

class FindPasswordFragment : Fragment() {

	lateinit var mainActivity: MainActivity
	lateinit var binding: FragmentFindPasswordBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentFindPasswordBinding.inflate(inflater, container, false)
		binding.btnBackFromFindPassword.setOnClickListener { mainActivity.goBack() }
			return binding.root
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		mainActivity = context as MainActivity
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.btnFindPasswordSendCode.setOnClickListener {
			binding.editFindPasswordCode.isVisible = true
			binding.btnFindPasswordCodeConfirm.isVisible = true
		}

		binding.btnFindPasswordCodeConfirm.setOnClickListener {
			binding.textFindPasswordShow.isVisible = true
		}
	}
}