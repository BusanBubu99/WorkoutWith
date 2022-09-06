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

data class UserEditProfileResponseData(@SerializedName("code") val code: Int)

/*
* name : 사용자 이름
profilePic : 프로필 사진
tags : 관심사 태그
userLocation : 동네 설정*/
data class UserEditProfileData(
    val name: String,
    val profilePic: String/*must be file!*/,
    val tags: String,
    val city: String,
    val country: String,
    val district: String
)


class UserEditProfileModule(override val userData: UserEditProfileData) : UserApiInterface {

    interface UserEditProfileInterface {
        @POST("/v1/profile/")
        fun get(
            @Body body: JsonObject
        ): Call<Any>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): Any? {
        try {
            var auth = UserAuthModule(null)
            val result = auth.getApiData()
            if (result == true) {
                //Do Any Operation or Jobs..
                val requestData = JsonObject()
                requestData.addProperty("token", userInformation.accessToken)
                requestData.addProperty("name", userData.name)
                requestData.addProperty("tags", userData.tags)
                val userLocation = JsonObject()
                userLocation.addProperty("city", userData.city)
                userLocation.addProperty("country", userData.country)
                userLocation.addProperty("district", userData.district)
                requestData.add("userLocation", userLocation)
                val retrofit = ApiClient.getApiClient()
                val retrofitObject = retrofit.create(UserEditProfileInterface::class.java)
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
            } else if (result is UserError) {
                //Auth Error Handling
                Log.d("error","AuthError")
                return result
            } else if (result is UninitializedPropertyAccessException) {
                //Auth Error Handling
                Log.d("Exception","AuthError")
                return result
            } else if (result is SocketTimeoutException) {
                //Auth Error Handling
                Log.d("Exception","AuthError")
                return result
            } else if (result is EOFException) {
                //Auth Error Handling
                Log.d("Exception","AuthError")
                return result
            } else if (result is Exception) {
                //Auth Error Handling
                Log.d("Exception","AuthError")
                return result
            } else {
                //What is This ?
                Log.d("Exception","What is this? ${result.toString()}")
                return result
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