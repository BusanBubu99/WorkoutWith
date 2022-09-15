package com.bubu.workoutwithclient.userinterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.MainScreenActivityBinding
import com.bubu.workoutwithclient.retrofitinterface.UserAddressKey
import com.bubu.workoutwithclient.retrofitinterface.userAddressKey

lateinit var gameCodeList : MutableList<String>

fun generateGameCode() {
	gameCodeList = mutableListOf()
	gameCodeList.add("축구")
	gameCodeList.add("농구")
	gameCodeList.add("풋살")
	gameCodeList.add("배드민턴")
	gameCodeList.add("런닝")
	gameCodeList.add("자전거")
	gameCodeList.add("볼링")
	gameCodeList.add("웨이트 트레이닝")
	gameCodeList.add("크로스핏")
	gameCodeList.add("테니스")
	gameCodeList.add("당구")
}

class MainScreenActivity : AppCompatActivity() {

	private lateinit var mBinding : MainScreenActivityBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		generateGameCode()
		userAddressKey = UserAddressKey
		userAddressKey.consumerKey = "Input User SGIS OpenAPI consumerkey"
		userAddressKey.consumerSecret= "Input User SGIS OpenAPI consumerSecret"

		mBinding = MainScreenActivityBinding.inflate(layoutInflater)

		setContentView(mBinding.root)

		val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

		val navController = navHostFragment.navController

		NavigationUI.setupWithNavController(mBinding.majorScreenNav, navController)
	}

	fun hideNavBar(state : Boolean) {
		if(state) mBinding.majorScreenNav.visibility = View.GONE else mBinding.majorScreenNav.visibility = View.VISIBLE
	}

	fun goBack() {
		onBackPressed()
	}
}