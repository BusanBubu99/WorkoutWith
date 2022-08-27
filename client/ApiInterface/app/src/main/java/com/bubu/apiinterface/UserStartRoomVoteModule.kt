package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import java.net.SocketTimeoutException

data class UserStartRoomVoteResponseData(
    @SerializedName("code") val code: Int,
    @SerializedName("error") val error: String
)

class UserStartRoomVoteModule(override val userData: JsonObject)
    : UserApiInterface<UserStartRoomVoteResponseData> {

    interface UserStartRoomVoteInterface {
        @Headers("Content-Type: application/json")
        @PUT("/v1/matching/vote/")
        fun get(
            //@Query("targetId") targetId : String
            @Body body: JsonObject
        ): Call<UserStartRoomVoteResponseData>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): UserStartRoomVoteResponseData? {
        val retrofit = ApiClient.getApiClient()
        val retrofitObject = retrofit.create(UserStartRoomVoteInterface::class.java)
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