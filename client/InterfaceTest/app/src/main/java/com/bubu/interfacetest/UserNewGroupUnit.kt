package com.bubu.interfacetest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class UserNewGroupUnit(val token : String, val groupName : String, val groupMember : List<String>) {
    private val userNewGroupData = JsonObject()

    init {
        userNewGroupData.addProperty("token", token)
        userNewGroupData.addProperty("groupName",groupName)
        userNewGroupData.addProperty("groupMember", groupMember) // List Error!
    }


    interface UserNewGroupInterface { //인터페이스
        @Headers("Content-Type: application/json")
        @POST("/v1/test/")
        fun get(
            @Body body: JsonObject
        ): Call<UserNewGroupResponseData>
    }

    data class UserNewGroupResponseData(
        @SerializedName("responseCode") override val responseCode: Int
    ) : UserResponseData//응답 받은 데이터

    suspend fun getApiData(): UserNewGroupResponseData? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://59.20.74.132:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserNewGroupInterface::class.java)
        var res = retrofitObject.get(userNewGroupData).execute().body()
        //var res = retrofitObject.get(userFeedData).execute()
        //if(res.headers()) Exception
        return res
    }
}