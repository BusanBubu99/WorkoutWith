package com.bubu.workoutwithclient.retrofitinterface

/**
 * UserData Object
 * Define UserData
 * */

lateinit var userInformation: UserData

object UserData {
    lateinit var userId : String
    lateinit var accessToken : String
    lateinit var refreshToken : String
}