package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import java.net.SocketTimeoutException

data class UserCreateRoomVoteResponse(val code : Int)


class UserCreateRoomVoteModule(override val userData: JsonObject)
    : UserApiInterface<UserCreateRoomVoteResponse> {

    interface UserCreateRoomVoteInterface {
        @Headers("Content-Type: application/json")
        @POST("/v1/matching/vote/")
        fun get(
            //@Query("targetId") targetId : String
            @Body body: JsonObject
        ): Call<UserCreateRoomVoteResponse>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): UserCreateRoomVoteResponse? {
        val retrofit = ApiClient.getApiClient()
        val retrofitObject = retrofit.create(UserCreateRoomVoteInterface::class.java)
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