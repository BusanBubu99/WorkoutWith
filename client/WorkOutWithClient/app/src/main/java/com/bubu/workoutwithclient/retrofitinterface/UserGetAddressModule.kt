package com.bubu.workoutwithclient.retrofitinterface

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.EOFException
import java.net.SocketTimeoutException

/**
 * UserGetAddressModule
 * Search for city, county, district.
 *
 * Parameter :
 * fun getApiData() : None
 * fun getCity() : None
 * fun getDetailCity() : City Code(String)
 *
 *
 * Return Value : UserError / UserCityResponseData
 * If communication is successful UserCityResponseData that is defined below
 * else UserError
 *
 * Exception :
 * SocketTimeOutException : if Server is closed
 * EOFException : Response Type Mismatch
 * Exception :
 * Exceptions we don't know yet
 * */


data class UserCityResponseData(val cityName: String, val cityCode: String)

class UserGetAddressModule(override val userData: Any? = null) : UserApiInterface {


    lateinit var cityAccessToken: String

    interface UserGetCityAccessKeyInterface {
        @GET("/OpenAPI3/auth/authentication.json")
        fun get(
            @Query("consumer_key") consumerKey: String = "91b8646a2a7041f18394",
            @Query("consumer_secret") consumerSecret: String = "e8ab5df1f96249dcbc15"
        ): Call<Any>
    }

    interface UserGetCityInterface {
        @GET("/OpenAPI3/addr/stage.json")
        fun get(
            @Query("accessToken") accessToken: String
        ): Call<Any>
        //보내는 데이터 형식
    }

    interface UserGetDetailCityInterface {
        @GET("/OpenAPI3/addr/stage.json")
        fun get(
            @Query("accessToken") accessToken: String,
            @Query("cd") cd: String
        ): Call<Any>
    }

    override suspend fun getApiData(): Any? {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sgisapi.kostat.go.kr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserGetCityAccessKeyInterface::class.java)
        try {
            var resp = retrofitObject.get().execute()
            if (resp.code() in 100..199) {
                return super.handle100(resp)
            } else if (resp.code() in 200..299) { //Successful!!
                var responseBody = super.handle200(resp)
                Log.d("response", responseBody.toString())
                val result = Gson().toJsonTree(responseBody).asJsonObject
                val tmp = result.getAsJsonObject("result")
                cityAccessToken = tmp.asJsonObject.get("accessToken").toString().replace("\"", "")
                Log.d("citiyadd", cityAccessToken)
                return getCity()
            } else if (resp.code() in 300..399) {
                return super.handle300(resp)
            } else if (resp.code() in 400..499) {
                return super.handle400(resp)
            } else {
                return super.handle500(resp)
            }
        } catch (e: SocketTimeoutException) {
            Log.d("TimeOutException Maybe Server Closed", e.toString())
            return e
        } catch (e: EOFException) {
            Log.d("EOFException Maybe Response Data Type Mismatch", e.toString())
            return e
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
            return e
        }
    }

    private suspend fun getCity(): Any? {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sgisapi.kostat.go.kr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserGetCityInterface::class.java)
        try {
            var resp = retrofitObject.get(cityAccessToken).execute()
            if (resp.code() in 100..199) {
                return super.handle100(resp)
            } else if (resp.code() in 200..299) { //Successful!!
                var responseBody = super.handle200(resp)
                Log.d("response", responseBody.toString())
                val result = Gson().toJsonTree(responseBody).asJsonObject
                val tmp = result.getAsJsonArray("result")
                var list = mutableListOf<UserCityResponseData>()
                tmp.forEach {
                    list.add(
                        UserCityResponseData(
                            it.asJsonObject.get("addr_name").toString().replace("\"", ""),
                            it.asJsonObject.get("cd").toString().replace("\"", "")
                        )
                    )
                }
                return list
            } else if (resp.code() in 300..399) {
                return super.handle300(resp)
            } else if (resp.code() in 400..499) {
                return super.handle400(resp)
            } else {
                return super.handle500(resp)
            }
        } catch (e: SocketTimeoutException) {
            Log.d("TimeOutException Maybe Server Closed", e.toString())
            return e
        } catch (e: EOFException) {
            Log.d("EOFException Maybe Response Data Type Mismatch", e.toString())
            return e
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
            return e
        }
    }

    suspend fun getDetailCity(code: String): Any? {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sgisapi.kostat.go.kr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserGetDetailCityInterface::class.java)
        try {
            var resp = retrofitObject.get(cityAccessToken, code).execute()
            if (resp.code() in 100..199) {
                return super.handle100(resp)
            } else if (resp.code() in 200..299) { //Successful!!
                var responseBody = super.handle200(resp)
                Log.d("response", responseBody.toString())
                val result = Gson().toJsonTree(responseBody).asJsonObject
                val tmp = result.getAsJsonArray("result")
                var list = mutableListOf<UserCityResponseData>()
                tmp.forEach {
                    list.add(
                        UserCityResponseData(
                            it.asJsonObject.get("addr_name").toString().replace("\"", ""),
                            it.asJsonObject.get("cd").toString().replace("\"", "")
                        )
                    )
                }
                return list
            } else if (resp.code() in 300..399) {
                return super.handle300(resp)
            } else if (resp.code() in 400..499) {
                return super.handle400(resp)
            } else {
                return super.handle500(resp)
            }
        } catch (e: SocketTimeoutException) {
            Log.d("TimeOutException Maybe Server Closed", e.toString())
            return e
        } catch (e: EOFException) {
            Log.d("EOFException Maybe Response Data Type Mismatch", e.toString())
            return e
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
            return e
        }
    }
}