package com.bubu.workoutwithclient.retrofitinterface

import android.util.Log
import com.google.gson.Gson
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

/**
 * UserLoginModule
 * Send User Login Data
 *
 * Parameter : UserLoginData
 *
 *
 * Return Value : UserError / UserLoginResponseData
 * If communication is successful UserLoginResponseData that is defined below
 * else UserError
 *
 * Exception :
 * SocketTimeOutException : if Server is closed
 * EOFException : Response Type Mismatch
 * Exception :
 * Exceptions we don't know yet
 * */

data class UserLoginResponseData(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)

data class UserLoginData(
    val username: String,
    val email: String,
    val password: String
)

class UserLoginModule(override val userData: UserLoginData) : UserApiInterface {

    interface UserLoginInterface {
        @Headers("Content-Type: application/json")
        @POST("/v1/login/")
        fun get(
            @Body body: JsonObject
        ): Call<Any>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): Any? {
        val retrofit = Retrofit.Builder()
            .baseUrl(super.serverAddress)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserLoginInterface::class.java)
        try {
            val requestData = JsonObject()
            requestData.addProperty("username",userData.username)
            requestData.addProperty("email",userData.email)
            requestData.addProperty("password",userData.password)
            var resp = retrofitObject.get(requestData).execute()
            if (resp.code() in 100..199) {
                return super.handle100(resp)
            } else if (resp.code() in 200..299) { //
                val responseBody = super.handle200(resp)
                val jsonObject: JsonObject = Gson().toJsonTree(responseBody).asJsonObject
                val accessToken = jsonObject.get("access_token").toString().replace("\"", "")
                val refreshToken = jsonObject.get("refresh_token").toString().replace("\"", "")
                val userJson = jsonObject.getAsJsonObject("user")
                val userId = userJson.get("username").toString().replace("\"", "")
                userInformation.userId = userId
                userInformation.accessToken = accessToken
                userInformation.refreshToken = refreshToken
                return UserLoginResponseData(accessToken, refreshToken)
            } else if (resp.code() in 300..399) {
                return super.handle300(resp)
            } else if (resp.code() in 400..499) {
                val errorResponse = super.handle400(resp)
                val errorMessages = mutableListOf<String>()
                val err = errorResponse.message[0]
                if (err.contains("Unable to log in with provided credentials.")) {
                    errorMessages.add("계정정보가 맞지 않습니다!")
                }
                return UserError(errorMessages)
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