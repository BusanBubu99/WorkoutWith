package com.bubu.interfacetest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class UserFriendUnit(val token : String, val targetId : String) {
    private val userFriendData = JsonObject()

    init {
      userFriendData.addProperty("token", token)
      userFriendData.addProperty("targetId", targetId)
    } //실제 보낼 데이터

    interface UserFriendInterface { //인터페이스
        @Headers("Content-Type: application/json")
        @POST("/v1/test/")
        fun get(
            @Body body: JsonObject
        ): Call<UserFriendResponseData>
        //보내는 데이터 형식
    }

    data class UserFriendResponseData(
        @SerializedName("responseCode") override val responseCode: Int
    ) : UserResponseData//응답 받은 데이터

    suspend fun getApiData(): UserFriendResponseData? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://59.20.74.132:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserFriendInterface::class.java)
        var res = retrofitObject.get(userFriendData).execute().body()
        //var res = retrofitObject.get(userFeedData).execute()
        //if(res.headers()) Exception
        return res
    }
}