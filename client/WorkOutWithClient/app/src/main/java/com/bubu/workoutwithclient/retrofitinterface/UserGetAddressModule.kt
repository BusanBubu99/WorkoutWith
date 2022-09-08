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

class UserGetAddressModule(override val userData: Any?) : UserApiInterface {


    lateinit var cityAccessToken : String

    interface UserGetCityAccessKeyInterface {
        @GET("/OpenAPI3/auth/authentication.json")
        fun get(
            @Query("consumer_key") consumerKey : String = "c30940feb989472cb36c",
            @Query("consumer_secret") consumerSecret : String = "981432ae72154a95b59b"
        ) : Call<Any>
    }

    interface UserGetCityInterface {
        //accessToken=7974f399-072c-414a-b064-8b15755279ea
        //
        @GET("/OpenAPI3/addr/stage.json")
        fun get(
            @Query("accessToken") accessToken : String
        ): Call<Any>
        //보내는 데이터 형식
    }

    interface UserGetDetailCityInterface {
        @GET("/OpenAPI3/addr/stage.json")
        fun get(
            @Query("accessToken") accessToken : String,
            @Query("cd") cd : String
        ) : Call<Any>
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
                Log.d("response",responseBody.toString())
                val result = Gson().toJsonTree(responseBody).asJsonObject
                val tmp = result.getAsJsonObject("result")
                cityAccessToken = tmp.asJsonObject.get("accessToken").toString().replace("\"","")
                Log.d("citiyadd",cityAccessToken)
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

    private suspend fun getCity() : Any? {
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
                Log.d("response",responseBody.toString())
                val result = Gson().toJsonTree(responseBody).asJsonObject
                val tmp = result.getAsJsonArray("result")
                var list = mutableListOf<JsonObject>()
                tmp.forEach {
                    var json = JsonObject()
                    json.addProperty("name",it.asJsonObject.get("addr_name").toString().replace("\"",""))
                    json.addProperty("cd",it.asJsonObject.get("cd").toString().replace("\"",""))
                    list.add(json)
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

    suspend fun getDetailCity(code : String) : Any? {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sgisapi.kostat.go.kr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserGetDetailCityInterface::class.java)
        try {
            var resp = retrofitObject.get(cityAccessToken,code).execute()
            if (resp.code() in 100..199) {
                return super.handle100(resp)
            } else if (resp.code() in 200..299) { //Successful!!
                var responseBody = super.handle200(resp)
                Log.d("response",responseBody.toString())
                val result = Gson().toJsonTree(responseBody).asJsonObject
                val tmp = result.getAsJsonArray("result")
                var list = mutableListOf<JsonObject>()
                tmp.forEach {
                    var json = JsonObject()
                    json.addProperty("name",it.asJsonObject.get("addr_name").toString().replace("\"",""))
                    json.addProperty("cd",it.asJsonObject.get("cd").toString().replace("\"",""))
                    list.add(json)
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