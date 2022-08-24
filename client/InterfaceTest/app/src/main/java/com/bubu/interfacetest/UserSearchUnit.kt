package com.bubu.interfacetest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class UserSearchUnit(val target : String, val count : Int) {
    private val userSearchData = JsonObject()

    init {
        userSearchData.addProperty("target",target)
        userSearchData.addProperty("count", count)
    }

    interface UserSearchInterface { //인터페이스
        @Headers("Content-Type: application/json")
        @POST("/v1/test/")
        fun get(
            @Body body: JsonObject
        ): Call<List<UserSearchResponseData>>
    }

    data class UserSearchResponseData(
        @SerializedName("responseCode") override val responseCode: Int,
        @SerializedName("id") val id : String, //사용자 아이디
        @SerializedName("name") val name : String, //사용자 이름
        @SerializedName("profilePic") val profilePic: file, //프로필 사진
        @SerializedName("tags") val tags: String //관심사
    ) : UserResponseData//응답 받은 데이터

    suspend fun getApiData(): List<UserSearchResponseData>? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://59.20.74.132:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserSearchInterface::class.java)
        var res = retrofitObject.get(userSearchData).execute().body()
        //var res = retrofitObject.get(userFeedData).execute()
        //if(res.headers()) Exception
        return res
    }
}