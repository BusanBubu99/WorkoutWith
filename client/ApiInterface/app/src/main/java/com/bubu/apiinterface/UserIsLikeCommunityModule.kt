package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.SocketTimeoutException

data class UserIsLikeCommunityResponseData(@SerializedName("likeable") val likeable : Boolean)

class UserIsLikeCommunityModule(override val userData: JsonObject)
    : UserApiInterface {

    interface UserIsLikeCommunityInterface {
        //@Headers("Content-Type: application/json")
        @GET("/v1/community/like/")
        fun get(
            @Query("postId") postId : String
            //@Body body: JsonObject
        ): Call<UserGetCommunityListResponseData>
        //보내는 데이터 형식
    }
    override suspend fun getApiData(): UserIsLikeCommunityResponseData? {
        val retrofit = ApiClient.getApiClient()
        val retrofitObject = retrofit.create(UserIsLikeCommunityInterface::class.java)
        try {
            var resp = retrofitObject.get(userData["postId"].toString()).execute()
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