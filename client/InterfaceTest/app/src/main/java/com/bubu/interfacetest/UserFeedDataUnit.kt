package com.bubu.interfacetest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class UserFeedDataUnit(val token : String, val count : Int) {
    private val userFeedData = JsonObject()//실제 보낼 데이터

    init {
        userFeedData.addProperty("token", token)
        userFeedData.addProperty("count", count)
    }

    interface UserFeedInterface { //인터페이스
        @Headers("Content-Type: application/json")
        @POST("/v1/test/")
        fun get(
            @Body body: JsonObject
        ): Call<List<UserFeedResponseData>>
        //보내는 데이터 형식
    }

    data class UserFeedResponseData(
        @SerializedName("responseCode") override val responseCode: Int,
        @SerializedName("_id") val id : String,
        @SerializedName("name") val name : String,
        @SerializedName("content") val content: String,
        @SerializedName("picture") val picture: file, //
        @SerializedName("timestamp") val timestamp: String, //시간형식
        @SerializedName("like") val like: obj,//
        @SerializedName("memberMax") val memberMax: Int,
        @SerializedName("game") val game: Int
    ) : UserResponseData//응답 받은 데이터

    suspend fun getApiData(): List<UserFeedResponseData>? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://59.20.74.132:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserFeedInterface::class.java)
        var res = retrofitObject.get(userFeedData).execute().body()
        //var res = retrofitObject.get(userFeedData).execute()
        //if(res.headers()) Exception
        return res
    }
}