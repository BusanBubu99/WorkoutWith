package com.bubu.workoutwithclient.retrofitinterface

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import java.io.EOFException
import java.net.SocketTimeoutException

data class UserGetMatchListResponseData(
    @SerializedName("matchId") val id: String,
    @SerializedName("title") val title: String
)

class UserGetMatchListModule(override val userData: Any?) : UserApiInterface {

    interface UserGetMatchListInterface {
        @GET("/v1/matching")
        fun get(
        ): Call<Any>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): Any? {
        try {
            var auth = UserAuthModule(null)
            val result = auth.getApiData()
            if (result == true) {
                //Do Any Operation or Jobs..
                val retrofit = ApiTokenHeaderClient.getApiClient()
                val retrofitObject = retrofit.create(UserGetMatchListInterface::class.java)
                try {
                    var resp = retrofitObject.get().execute()
                    if (resp.code() in 100..199) { //
                        return super.handle100(resp)
                    } else if (resp.code() in 200..299) {
                        val responseBody = super.handle200(resp)
                        val jsonString : String = Gson().toJsonTree(responseBody).asJsonArray.toString()
                        return Gson().fromJson(jsonString,Array<UserGetMatchListResponseData>::class.java).toList()
                        /*val list = mutableListOf<UserGetMatchLists>()
                        jsonObject.forEach {
                            list.add(
                                UserGetMatchLists(
                                    it.asJsonObject.get("matchId").toString(),
                                    it.asJsonObject.get("title").toString()
                                )
                            )
                        }
                        return UserGetMatchListResponseData(list)*/
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