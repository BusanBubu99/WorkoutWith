package com.bubu.workoutwithclient.retrofitinterface

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.EOFException
import java.net.SocketTimeoutException

/**
 * UserLikeModule
 * Like the User Profile
 *
 * Parameter : UserLikeData
 *
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

data class UserLikeData(
    val targetId: String, val matchId: String
)

class UserLikeModule(override val userData: UserLikeData) : UserApiInterface {

    interface UserLikeInterface {
        @Headers("Content-Type: application/json")
        @POST("/v1/matching/vote/like/")
        fun get(
            @Body body: JsonObject
        ): Call<Any>
    }

    override suspend fun getApiData(): Any? {
        try {
            var auth = UserAuthModule(null)
            val result = auth.getApiData()
            if (result == true) {
                val retrofit = ApiTokenHeaderClient.getApiClient()
                val retrofitObject = retrofit.create(UserLikeInterface::class.java)
                try {
                    val requestData = JsonObject()
                    requestData.addProperty("targetId", userData.targetId)
                    requestData.addProperty("matchId", userData.matchId)
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