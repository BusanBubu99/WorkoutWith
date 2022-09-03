package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.EOFException
import java.net.SocketTimeoutException


data class UserLoginResponseData(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user") val user : UserLoginUserData
)

data class UserLoginUserData(
    @SerializedName("pk") val pk: Int,
    @SerializedName("username") val userName: String,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String
)

/**
 *
 * {
 *  "access_token": "",
 *  "refresh_token": "",
 *  "user": {
 *          "pk": 7,
 *          "username": "gbdngb50000",
 *          "email": "gbdngb50000@naver.com",
 *          "first_name": "",
 *          "last_name": ""
 *      }
 * }
 *
 * */

class UserLoginModule(override val userData: JsonObject) : UserApiInterface {

    interface UserLoginInterface {
        @Headers("Content-Type: application/json")
        @POST("/api/accounts/v1/login/")
        fun get(
            @Body body: JsonObject
        ): Call<UserLoginResponseData>
        //보내는 데이터 형식
    }


    override suspend fun getApiData(): UserLoginResponseData? {
        val retrofit = Retrofit.Builder()
            .baseUrl(super.serverAddress)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserLoginInterface::class.java)
        try {
            var resp = retrofitObject.get(userData).execute()
            if (resp.code() in 100..199) {
                Log.d("response Code", resp.code().toString())
                return null
            } else if (resp.code() in 200..299) { //
                Log.d("response Code", resp.code().toString())
                Log.d("response", resp.body().toString())
                return resp.body()
            } else if (resp.code() in 300..399) {
                Log.d("response Code", resp.code().toString())
                return null
            } else if (resp.code() in 400..499) {
                Log.d("response Code", resp.code().toString())
                return null
            } else {
                Log.d("response Code", resp.code().toString())
                return null
            }
        } catch (e: SocketTimeoutException) {
            Log.d("TimeOutException Maybe Server Closed", e.toString())
            return null
        } catch (e: EOFException) {
            Log.d("EOFException Maybe Response Data Type Mismatch", e.toString())
            return null
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
            return null
        }
    }
}