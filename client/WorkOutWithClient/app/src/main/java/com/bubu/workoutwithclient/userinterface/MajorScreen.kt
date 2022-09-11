package com.bubu.workoutwithclient.userinterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.ActivityMajorScreenBinding

class MajorScreen : AppCompatActivity() {

	private lateinit var mBinding : ActivityMajorScreenBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		mBinding = ActivityMajorScreenBinding.inflate(layoutInflater)

		setContentView(mBinding.root)

		val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

		val navController = navHostFragment.navController

		NavigationUI.setupWithNavController(mBinding.majorScreenNav, navController)
	}

	fun hideNavBar(state : Boolean) {
		if(state) mBinding.majorScreenNav.visibility = View.GONE else mBinding.majorScreenNav.visibility = View.VISIBLE
	}

}