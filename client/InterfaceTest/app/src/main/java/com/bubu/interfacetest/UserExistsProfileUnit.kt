package com.bubu.interfacetest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class UserExistProfileUnit(val token : String) {
    private val UserExistsProfileData = JsonObject() //실제 보낼 데이터

    init {
        UserExistsProfileData.addProperty("token", token)
    }

    interface UserExistsProfileInterface { //인터페이스
        @Headers("Content-Type: application/json")
        @POST("/v1/test/")
        fun get(
            @Body body: JsonObject
        ): Call<UserExistsProfileResponseData>
    }

    data class UserExistsProfileResponseData(
        @SerializedName("responseCode") override val responseCode: Int
    ):UserResponseData//응답 받은 데이터

    suspend fun getApiData(): UserExistsProfileResponseData? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://59.20.74.132:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserExistsProfileInterface::class.java)
        var res = retrofitObject.get(UserExistsProfileData).execute().body()
        //var res = retrofitObject.get(userFeedData).execute()
        //if(res.headers()) Exception
        return res
    }

}