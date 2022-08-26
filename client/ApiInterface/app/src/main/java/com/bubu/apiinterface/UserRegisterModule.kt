package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.net.SocketTimeoutException


data class UserRegisterResponse(@SerializedName("token") val token: UserRegisterResponse?)

class UserRegisterModule(override val userData: JsonObject, override val token: String) : UserApiInterface<JsonObject,UserRegisterResponse>{

    interface UserRegisterInterface {
        @Headers("Content-Type: application/json")
        @POST("/v1/register/")
        fun get(
            @Body body: JsonObject
        ): Call<UserRegisterResponse>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): UserRegisterResponse? {
        val retrofit = Retrofit.Builder()
            .baseUrl(super.serverAddress)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserRegisterInterface::class.java)
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
