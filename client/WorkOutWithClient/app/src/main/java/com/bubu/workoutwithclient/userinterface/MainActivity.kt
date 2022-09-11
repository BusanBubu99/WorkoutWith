package com.bubu.workoutwithclient.userinterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.retrofitinterface.UserData
import com.bubu.workoutwithclient.retrofitinterface.userInformation

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setLoginFragment()
		userInformation = UserData
	}

	fun setLoginFragment() {
		val loginFragment: LoginFragment = LoginFragment()
		val transaction = supportFragmentManager.beginTransaction()
		transaction.add(R.id.MainFrameLayout, loginFragment)
		transaction.commit()
	}

	fun goRegisterFragment() {
		val registerFragment = RegisterFragment()
		val transaction = supportFragmentManager.beginTransaction()
		transaction.add(R.id.MainFrameLayout, registerFragment)
		transaction.addToBackStack("detail")
		transaction.commit()
	}

	fun goFindIdFragment() {
		val findIdFragment = FindIdFragment()
		val transaction = supportFragmentManager.beginTransaction()
		transaction.add(R.id.MainFrameLayout, findIdFragment)
		transaction.addToBackStack("detail")
		transaction.commit()
	}

	fun goFindPasswordFragment() {
		val findPasswordFragment = FindPasswordFragment()
		val transaction = supportFragmentManager.beginTransaction()
		transaction.add(R.id.MainFrameLayout, findPasswordFragment)
		transaction.addToBackStack("detail")
		transaction.commit()
	}

	fun goNewProfileFragment() {
		val newProfileFragment = NewProfileFragment()
		val transaction = supportFragmentManager.beginTransaction()
		transaction.add(R.id.MainFrameLayout, newProfileFragment)
		transaction.addToBackStack("detail")
		transaction.commit()
	}
	fun goBack() {
		onBackPressed()
	}
}