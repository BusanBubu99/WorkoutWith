package com.bubu.apiinterface

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.EOFException
import java.net.SocketTimeoutException

data class UserStartMatchResponseData(@SerializedName("matchId") val matchId: Int)

data class UserStartMatchData(
    val city: String, val county: String, val district: String,
    val game: Int
)

class UserStartMatchModule(override val userData: UserStartMatchData) : UserApiInterface {

    interface UserStartMatchInterface {
        @Headers("Content-Type: application/json")
        @POST("/v1/matching/")
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
                val retrofitObject = retrofit.create(UserStartMatchInterface::class.java)
                try {
                    val requestData = JsonObject()
                    requestData.addProperty("token", userInformation.accessToken)
                    requestData.addProperty("city", userData.city)
                    requestData.addProperty("county", userData.county)
                    requestData.addProperty("district", userData.district)
                    requestData.addProperty("game", userData.game)
                    var resp = retrofitObject.get(requestData).execute()
                    if (resp.code() in 100..199) {
                        return super.handle100(resp)
                    } else if (resp.code() in 200..299) {
                        val responseBody = super.handle200(resp)
                        val jsonString: String = Gson().toJsonTree(responseBody).asJsonObject.toString()
                        return convertToClass(jsonString,UserStartMatchResponseData::class.java)
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