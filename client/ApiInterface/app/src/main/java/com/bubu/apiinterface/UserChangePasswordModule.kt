package com.bubu.apiinterface

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

data class UserChangePasswordData(val password1 : String, val password2 : String)

class UserChangePasswordModule(override val userData: UserChangePasswordData)
    : UserApiInterface {

    interface UserChangePasswordInterface {
        @Headers("Content-Type: application/json")
        @POST("/v1/user/")
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
        val retrofitObject = retrofit.create(UserChangePasswordInterface::class.java)
        try {
            val requestData = JsonObject()
            requestData.addProperty("password1",userData.password1)
            requestData.addProperty("password2",userData.password2)
            var resp = retrofitObject.get(requestData).execute()
            if (resp.code() in 100..199) {
                return super.handle100(resp)
            } else if (resp.code() in 200..299) {
                return resp.code()
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