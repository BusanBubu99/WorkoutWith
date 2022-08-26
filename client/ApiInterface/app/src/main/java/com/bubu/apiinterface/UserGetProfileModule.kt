package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.net.SocketTimeoutException

data class UserGetProfileResponseData(
    @SerializedName("userId") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("profilePic") val profilePic: String,//file
    @SerializedName("tags") val tags: String,
    @SerializedName("communityPost") val communityPost: List<UserGetProfileCommunityPostResponseData>
)

data class UserGetProfileCommunityPostResponseData(
    @SerializedName("postUserId") val postUserId: String,
    @SerializedName("postId") val postId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("picture") val picture: String,//file
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("like") val like: Int
)

class UserGetProfileModule(override val userData: String, override val token: String) : UserApiInterface<String,UserGetProfileResponseData> {

    interface UserGetProfileInterface {
        //@Headers("Content-Type: application/json")
        @GET("/v1/user/")
        fun get(
            @Query("targetId") targetId : String
            //@Body body: JsonObject
        ): Call<UserEditProfileResponseData>
        //보내는 데이터 형식
    }


    override suspend fun getApiData(): UserGetProfileResponseData? {
        val retrofit = Retrofit.Builder()
            .baseUrl(super.serverAddress)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserGetProfileInterface::class.java)
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