package com.bubu.interfacetest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class UserProfileDataUnit(val targetId : String) {
    private val userProfileData = JsonObject()

    init {
        userProfileData.addProperty("targetId", targetId)
    }

    interface UserProfileDataInterface { //인터페이스
        @Headers("Content-Type: application/json")
        @POST("/v1/test/")
        fun get(
            @Body body: JsonObject
        ): Call<List<UserProfileResponseData>>
    }

    data class UserProfileResponseData(
        @SerializedName("responseCode") override val responseCode: Int,
        @SerializedName("name") val name : String,
        @SerializedName("profilePic") val profilePic : file, //
        @SerializedName("tags") val tags : String,
        @SerializedName("feeds") val feeds: List<UserFeedDataUnit.UserFeedResponseData>,
    ) : UserResponseData//응답 받은 데이터

    suspend fun getApiData(): List<UserProfileResponseData>? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://59.20.74.132:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserProfileDataInterface::class.java)
        try {
            var resp = retrofitObject.get(userProfileData).execute().body()
            if(resp.isSuccessful){
                return resp.body()
            } else {
                //error
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