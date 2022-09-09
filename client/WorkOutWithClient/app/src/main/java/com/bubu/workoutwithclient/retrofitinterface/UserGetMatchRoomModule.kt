package com.bubu.workoutwithclient.retrofitinterface

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*
import java.io.EOFException
import java.net.SocketTimeoutException

/**
 * UserGetMatchRoomModule
 * View specific Match rooms
 *
 * Parameter : UserGetMatchRoomData
 * shown below
 *
 *
 * Return Value : UserError / UserGetMatchRoomResponseData
 * If communication is successful UserGetMatchRoomResponseData that is defined below
 * else UserError
 *
 * Exception :
 * SocketTimeOutException : if Server is closed
 * EOFException : Response Type Mismatch
 * Exception :
 * Exceptions we don't know yet
 * */

data class UserGetMatchRoomResponseData(
    @SerializedName("userInfo") val userInfo: List<UserGetMatchRoomUserInfo>,
    @SerializedName("voteInfo") val voteInfo: List<UserGetMatchRoomVoteInfo>
)

data class UserGetMatchRoomUserInfo(
    @SerializedName("profilePic") val profilePic: String,//file
    @SerializedName("name") val name: String
)

data class UserGetMatchRoomVoteInfo(
    @SerializedName("voteTitle") val voteTitle: String,
    @SerializedName("voteId") val voteId: Int
)

data class UserGetMatchRoomData(val matchId: Int)

class UserGetMatchRoomModule(override val userData: UserGetMatchRoomData) : UserApiInterface {
    interface UserGetMatchRoomInterface {
        @GET("/v1/matching/info")
        fun get(
            @Query("matchId") matchId: Int
        ): Call<Any>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): Any? {
        try {
            var auth = UserAuthModule(null)
            val result = auth.getApiData()
            if (result == true) {
                val retrofit = ApiTokenHeaderClient.getApiClient()
                val retrofitObject = retrofit.create(UserGetMatchRoomInterface::class.java)
                try {
                    var resp =
                        retrofitObject.get(userData.matchId).execute()
                    if (resp.code() in 100..199) {
                        return super.handle100(resp)
                    } else if (resp.code() in 200..299) {
                        val responseBody = super.handle200(resp)
                        val jsonString: String = Gson().toJsonTree(responseBody).asJsonObject.toString()
                        return convertToClass(jsonString, UserGetMatchRoomResponseData::class.java)
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