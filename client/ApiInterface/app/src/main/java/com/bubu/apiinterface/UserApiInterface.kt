package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException


/**
 * UserApiInterface
 * Defines the interface for the client's retrofit API.
 *
 * Generic : <D, I, R> :
 * D : UserData
 * I : Retrofit Interface
 * R : ResponseUserData
 *
 * Parameter :
 * tokenParam(String) : LoginToken
 * requestMethod(String) : Retrofit Request Method
 * uri(String) : Retrofit URI
 *
 * Return Value : R
 * R : Retrofit Response Value
 *
 * Exception :
 * if Server Closed then => SocketTimeoutException
 * else Exception
 *
* */
//server http response code
const val OK : Int = 200


interface UserApiInterface<D,R> {
    val token : String
    val userData : D
    val serverAddress : String
        get() = "http://127.0.0.1:8000"
    abstract suspend fun getApiData() : R?
}

