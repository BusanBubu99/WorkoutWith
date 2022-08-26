package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.net.SocketTimeoutException

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

class UserGetMatchRoomModule(override val token: String, override val userData: JsonObject)
    : UserApiInterface<JsonObject, UserGetMatchRoomResponseData> {
    interface UserGetMatchRoomInterface {
        //@Headers("Content-Type: application/json")
        @GET("/v1/matching/")
        fun get(
            @Query("matchId") token : String
            //@Body body: JsonObject
        ): Call<UserStartMatchResponseData>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): UserGetMatchRoomResponseData? {
        val retrofit = Retrofit.Builder()
            .baseUrl(super.serverAddress)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserStartMatchModule.UserStartMatchInterface::class.java)
        try {
            var resp = retrofitObject.get(userData).execute()
            if(resp.code() == OK) { //
                Log.d("response Code", resp.code().toString())
                Log.d("response", resp.body().toString())
                //Parsing Here!
                //...
                //....
                return null
            } else if (resp.code() == 300) {
                Log.d("response Code", resp.code().toString())
                return null
            } else {
                return null
            }
        } catch (e : SocketTimeoutException) {
            Log.d("TimeOutException",e.toString())
            return null
        } catch (e : Exception) {
            Log.d("Exception", e.toString())
            return null
        }
    }
}