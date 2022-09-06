package com.bubu.apiinterface

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException


/**
 * UserApiInterface
 * Defines the interface for the client's retrofit API.
 *
 * Generic : <R> :
 * R : ResponseUserData
 *
 * Parameter :
 * userData : Json UserData
 *
 * Return Value : R
 * R : Retrofit Response Value
 *
 * Exception :
 * if Server Closed then => SocketTimeoutException
 * else Exception
 *
* */
const val OK = 200
interface UserApiInterface {
    val userData : Any?
    val serverAddress: String
        get() = "http://14.44.115.2:8000"
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
object ApiClient {
    private const val BASE_URL = "http://14.44.115.2:8000"
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
                //.addHeader("Auth", userInformation.token)
                //.addHeader("Content","application/json")
                //.addHeader("()","()")
                .build()
            proceed(newRequest)
        }
    }
}



