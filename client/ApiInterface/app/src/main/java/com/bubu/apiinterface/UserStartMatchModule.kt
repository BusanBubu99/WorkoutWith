package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.net.SocketTimeoutException

data class UserStartMatchResponseData(@SerializedName("matchId") val matchId : Int)

class UserStartMatchModule(override val token: String, override val userData: JsonObject)
    : UserApiInterface<JsonObject,UserStartMatchResponseData> {

    interface UserStartMatchInterface {
        @Headers("Content-Type: application/json")
        @POST("/v1/matching/")
        fun get(
            @Body body: JsonObject
        ): Call<UserStartMatchResponseData>
        //보내는 데이터 형식
    }
    override suspend fun getApiData(): UserStartMatchResponseData? {
        val retrofit = Retrofit.Builder()
            .baseUrl(super.serverAddress)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserStartMatchInterface::class.java)
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