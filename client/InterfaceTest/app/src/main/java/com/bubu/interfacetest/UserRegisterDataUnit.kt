package com.bubu.interfacetest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


class UserRegisterDataUnit(val id : String, val password : String, val email : String) {
    private val userRegisterData = JsonObject()

    init {
        userRegisterData.addProperty("id", id)
        userRegisterData.addProperty("password",password)
        userRegisterData.addProperty("email",email)
    } //실제 보낼 데이터

    interface UserRegisterInterface { //인터페이스
        @Headers("Content-Type: application/json")
        @POST("/v1/test/")
        fun get(
            @Body body: JsonObject
        ): Call<UserRegisterResponseData>
    }

    data class UserRegisterResponseData(
        @SerializedName("responseCode") override val responseCode: Int
    ) : UserResponseData//응답 받은 데이터

    suspend fun getApiData(): UserRegisterResponseData? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://59.20.74.132:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserRegisterInterface::class.java)
        var res = retrofitObject.get(userRegisterData).execute().body()
        //var res = retrofitObject.get(userFeedData).execute()
        //if(res.headers()) Exception
        return res
    }
}