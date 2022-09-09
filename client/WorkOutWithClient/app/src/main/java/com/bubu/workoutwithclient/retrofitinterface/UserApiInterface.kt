package com.bubu.workoutwithclient.retrofitinterface

import android.util.Log
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


/**
 * UserApiInterface
 * define essential retrofit Interface
 *
 * Parameter : userData
 * predefined UserData class
 *
 * Return Value : UserError / UserData
 * If communication is successful UserData
 * else UserError
 *
 * Exception :
 * SocketTimeOutException : if Server is closed
 * EOFException : Response Type Mismatch
 * Exception :
 * Exceptions we don't know yet
 * */

interface UserApiInterface {
    val userData : Any?
    val serverAddress: String
        get() = "http://192.168.200.103:8000"
    suspend fun getApiData() : Any?
    suspend fun handle100(resp : retrofit2.Response<Any>) : UserError {
        var errorMessages = mutableListOf<String>()
        val err = resp.errorBody()!!.string()
        Log.d("response Code", resp.code().toString())
        Log.d("response Type", resp.errorBody()!!.javaClass.name)
        Log.d("response Message", err)
        errorMessages.add(err)
        return UserError(errorMessages)
    }
    suspend fun handle200(resp : retrofit2.Response<Any>) : Any? {
        Log.d("response Code", resp.code().toString())
        Log.d("response Type",resp?.javaClass!!.name)
        Log.d("response Message", resp.body().toString())
        return resp.body()
    }
    suspend fun handle300(resp : retrofit2.Response<Any>) : UserError {
        var errorMessages = mutableListOf<String>()
        val err = resp.errorBody()!!.string()
        Log.d("response Code", resp.code().toString())
        Log.d("response Type", resp.errorBody()!!.javaClass.name)
        Log.d("response Message", err)
        errorMessages.add(err)
        return UserError(errorMessages)
    }
    suspend fun handle400(resp : retrofit2.Response<Any>) : UserError {
        var errorMessages = mutableListOf<String>()
        val err = resp.errorBody()!!.string()
        Log.d("response Code", resp.code().toString())
        Log.d("response Type", resp.errorBody()!!.javaClass.name)
        Log.d("response Message", err)
        errorMessages.add(err)
        return UserError(errorMessages)
    }
    suspend fun handle500(resp : retrofit2.Response<Any>) : UserError {
        var errorMessages = mutableListOf<String>()
        val err = resp.errorBody()!!.string()
        Log.d("response Code", resp.code().toString())
        Log.d("response Type", resp.errorBody()!!.javaClass.name)
        Log.d("response Message", err)
        errorMessages.add(err)
        return UserError(errorMessages)
    }
    suspend fun convertToClass(jsonString : String, className : Class<*>?) : Any? {
        return Gson().fromJson(jsonString, className)
    }
}
/*
* */
object ApiTokenHeaderClient {
    private const val BASE_URL = "http://192.168.200.103:8000"
    fun getApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient
            = OkHttpClient.Builder().run {
        addInterceptor(interceptor)
        build()
    }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "Bearer ${userInformation.accessToken}")
                //.addHeader("Content","application/json")
                //.addHeader("()","()")
                .build()
            proceed(newRequest)
        }
    }
}



