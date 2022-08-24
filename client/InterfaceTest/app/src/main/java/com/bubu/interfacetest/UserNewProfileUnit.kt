package com.bubu.interfacetest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class UserNewProfileUnit( val token: String, val name: String, val profilePic: file, val tags : String) {
    private val userNewProfileData = JsonObject()

    init {
        userNewProfileData.addProperty("token",token)
        userNewProfileData.addProperty("name", name)
        userNewProfileData.addProperty("profilePic", profilePic)
        userNewProfileData.addProperty("tags", tags)
    }

    interface UserNewProfileInterface { //인터페이스
        @Headers("Content-Type: application/json")
        @POST("/v1/test/")
        fun get(
            @Body body: JsonObject
        ): Call<UserNewProfileResponseData>
    }

    data class UserNewProfileResponseData(
        @SerializedName("responseCode") override val responseCode: Int
    ) : UserResponseData//응답 받은 데이터

    suspend fun getApiData(): UserNewProfileResponseData? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://59.20.74.132:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserNewProfileInterface::class.java)
        try {
            var res = retrofitObject.get(userNewProfileData).execute().body()
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