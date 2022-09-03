package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
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
//server http response code
const val OK : Int = 200


interface UserApiInterface {
    val userData : JsonObject
    val serverAddress: String
        get() = "http://192.168.0.10:8000"
    suspend fun getApiData() : Any?
}

object ApiClient {
    private const val BASE_URL = "http://192.168.0.10:8000"
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



