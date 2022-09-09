package com.bubu.workoutwithclient.retrofitinterface

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.EOFException
import java.net.SocketTimeoutException

/**
 * UserRegisterModule
 * Send User Register Data
 *
 * Parameter : UserRegisterData
 * shown below
 *
 * Return Value : UserError / Http Code
 * If communication is successful Http Code that is defined below
 * else UserError
 *
 * Exception :
 * SocketTimeOutException : if Server is closed
 * EOFException : Response Type Mismatch
 * Exception :
 * Exceptions we don't know yet
 * */

data class UserRegisterData(
    val username: String,
    val email: String,
    val password1: String,
    val password2: String
)

class UserRegisterModule(override val userData: UserRegisterData) : UserApiInterface {

    interface UserRegisterInterface {
        @Headers("Content-Type: application/json")
        @POST("/v1/registration/")
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
        val retrofitObject = retrofit.create(UserRegisterInterface::class.java)
        try {
            val requestData = JsonObject()
            requestData.addProperty("username", userData.username)
            requestData.addProperty("email", userData.email)
            requestData.addProperty("password1", userData.password1)
            requestData.addProperty("password2", userData.password2)
            var resp = retrofitObject.get(requestData).execute()
            if (resp.code() in 100..199) {
                return super.handle100(resp)
            } else if (resp.code() in 200..299) { //Successful!!
                return resp.code()
            } else if (resp.code() in 300..399) {
                return super.handle300(resp)
            } else if (resp.code() in 400..499) {
                val errorResponse = super.handle400(resp)
                val errorMessages = mutableListOf<String>()
                val err = errorResponse.message[0]
                if (err.contains("password")) {
                    errorMessages.add("비밀번호를 다시 입력하세요!")
                }
                if (err.contains("username")) {
                    errorMessages.add("아이디를 다시 입력하세요!")
                }
                if (err.contains("email")) {
                    errorMessages.add("이메일을 다시 입력하세요!")
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
