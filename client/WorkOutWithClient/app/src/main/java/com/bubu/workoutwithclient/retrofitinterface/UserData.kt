package com.bubu.workoutwithclient.retrofitinterface


lateinit var userInformation: UserData

object UserData {
    lateinit var userId : String
    lateinit var accessToken : String
    lateinit var refreshToken : String
}