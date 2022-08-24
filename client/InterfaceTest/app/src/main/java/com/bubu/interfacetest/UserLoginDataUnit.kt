package com.bubu.interfacetest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class UserLoginDataUnit(val id : String, val password : String) {

    private val UserLoginData = JsonObject() //실제 보낼 데이터

    init {
        UserLoginData.addProperty("id",id)
        UserLoginData.addProperty("password",password)
    }

    interface UserLoginInterface { //인터페이스
        @Headers("Content-Type: application/json")
        @POST("/v1/test/")
        fun get(
            @Body body: JsonObject
        ): Call<UserLoginResponseData>
    }

    data class UserLoginResponseData(
        @SerializedName("responseCode") override val responseCode: Int
    ) : UserResponseData//응답 받은 데이터

    suspend fun getApiData(): UserLoginResponseData? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://59.20.74.132:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserLoginInterface::class.java)
        try {
            var resp = retrofitObject.get(UserLoginData).execute().body()
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