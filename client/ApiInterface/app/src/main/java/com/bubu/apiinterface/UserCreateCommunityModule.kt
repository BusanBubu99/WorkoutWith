package com.bubu.apiinterface

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.EOFException
import java.net.SocketTimeoutException

data class UserCreateCommunityData(
    val token: String, val title: String, val picture: String/**/,
    val content: String
)

class UserCreateCommunityModule(override val userData: UserCreateCommunityData) : UserApiInterface {

    interface UserCreateCommunityInterface {
        @Headers("Content-Type: application/json")
        @PUT("/v1/community/")
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
                val retrofit = ApiClient.getApiClient()
                val retrofitObject = retrofit.create(UserCreateCommunityInterface::class.java)
                try {
                    val requestData = JsonObject()
                    requestData.addProperty("token", userInformation.accessToken)
                    requestData.addProperty("title", userData.title)
                    requestData.addProperty("picture", userData.picture)//
                    requestData.addProperty("content", userData.content)
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
                    Log.d("TimeOutException", e.toString())
                    return e
                } catch (e: Exception) {
                    Log.d("Exception", e.toString())
                    return e
                } catch (e: EOFException) {
                    Log.d("EOFException Maybe Response Data Type Mismatch", e.toString())
                    return e
                }
            } else if (result is UserError) {
                //Auth Error Handling
                Log.d("error", "AuthError")
                return result
            } else if (result is UninitializedPropertyAccessException) {
                //Auth Error Handling
                Log.d("Exception", "AuthError")
                return result
            } else if (result is SocketTimeoutException) {
                //Auth Error Handling
                Log.d("Exception", "AuthError")
                return result
            } else if (result is EOFException) {
                //Auth Error Handling
                Log.d("Exception", "AuthError")
                return result
            } else if (result is Exception) {
                //Auth Error Handling
                Log.d("Exception", "AuthError")
                return result
            } else {
                //What is This ?
                Log.d("Exception", "What is this? ${result.toString()}")
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