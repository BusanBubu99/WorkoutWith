package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.net.SocketTimeoutException

data class UserLikeCommunityResponseData(val code : Int)

class UserLikeCommunityModule(override val userData: JsonObject)
    : UserApiInterface {
    interface UserLikeCommunityInterface {
        //@Headers("Content-Type: application/json")
        @POST("/v1/community/like/")
        fun get(
            //@Query("token") token : String
            @Body body: JsonObject
        ): Call<UserLikeCommunityResponseData>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): UserLikeCommunityResponseData? {
        val retrofit = ApiClient.getApiClient()
        val retrofitObject = retrofit.create(UserLikeCommunityInterface::class.java)
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