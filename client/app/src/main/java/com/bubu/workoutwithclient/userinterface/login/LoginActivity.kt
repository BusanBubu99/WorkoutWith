package com.bubu.workoutwithclient.userinterface.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.retrofitinterface.UserData
import com.bubu.workoutwithclient.retrofitinterface.userInformation

class LoginActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.login_activity)
		setLoginFragment()
		userInformation = UserData

	}

	fun setLoginFragment() {
		val loginFragment: LoginFragment = LoginFragment()
		val transaction = supportFragmentManager.beginTransaction()
		transaction.replace(R.id.MainFrameLayout, loginFragment)
		transaction.commit()
	}

	fun goLoginRegisterFragment() {
		val loginRegisterFragment = LoginRegisterFragment()
		val transaction = supportFragmentManager.beginTransaction()
		transaction.replace(R.id.MainFrameLayout, loginRegisterFragment)
		transaction.addToBackStack("detail")
		transaction.commit()
	}

	fun goLoginCreateNewProfileFragment() {
		val loginCreateNewProfileFragment = LoginCreateNewProfileFragment()
		val transaction = supportFragmentManager.beginTransaction()
		transaction.replace(R.id.MainFrameLayout, loginCreateNewProfileFragment)
		transaction.addToBackStack("detail")
		transaction.commit()
	}
	fun goBack() {
		onBackPressed()
	}
}