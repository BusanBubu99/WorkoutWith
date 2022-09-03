package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.EOFException
import java.net.SocketTimeoutException


//HTTP Response 만확인하면 될듯
data class UserRegisterResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user") val user: UserRegisterUserData
)

data class UserRegisterUserData(
    @SerializedName("pk") val pk: Int,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String
)

/**
 * {
 *  "access_token": "",
 *  "refresh_token": "",
 *  "user": {
 *  "pk": 1,
 *  "username": "gbdngb12",
 *  "email": "gbdngb12@Naver.com",
 *  "first_name": "",
 *  "last_name": ""
 *  }
 * }
 * */

class UserRegisterModule(override val userData: JsonObject) :
    UserApiInterface {

    interface UserRegisterInterface {
        @Headers("Content-Type: application/json")
        @POST("/api/accounts/v1/registration/")
        fun get(
            @Body body: JsonObject
        ): Call<Any>
        //보내는 데이터 형식
    }
    fun test(res : Any) {

    }

    override suspend fun getApiData(): Any? {
        val retrofit = Retrofit.Builder()
            .baseUrl(super.serverAddress)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserRegisterInterface::class.java)
        try {
            var resp = retrofitObject.get(userData).execute()
            var errorMessages = mutableListOf<String>()
            if (resp.code() in 100..199) {
                test(resp)
                Log.d("response Code", resp.code().toString())
                Log.d("response Type", resp.errorBody()!!.javaClass.name)
                Log.d("response Message", resp.errorBody()!!.string())
                errorMessages.add(resp.errorBody()!!.string())
                return UserError(errorMessages)
            } else if (resp.code() in 200..299) { //Successful!!
                Log.d("response Code", resp.code().toString())
                Log.d("response", resp.body().toString())
                //Log.d("response Message", resp.errorBody()!!.string())
                return resp.body()
            } else if (resp.code() in 300..399) {
                Log.d("response Code", resp.code().toString())
                Log.d("response Type", resp.errorBody()!!.javaClass.name)
                Log.d("response Message", resp.errorBody()!!.string())
                errorMessages.add(resp.errorBody()!!.string())
                return UserError(errorMessages)
            } else if (resp.code() in 400..499) {
                Log.d("response Code", resp.code().toString())
                Log.d("response Type", resp.errorBody()!!.javaClass.name)
                val err = resp.errorBody()!!.string()
                Log.d("response Message", err)
                if(err.contains("password")) {
                    errorMessages.add("비밀번호를 다시 입력하세요!")
                }
                if(err.contains("username")) {
                    errorMessages.add("아이디를 다시 입력하세요!")
                }
                if(err.contains("email")) {
                    errorMessages.add("이메일을 다시 입력하세요!")
                }
                return UserError(errorMessages)
            } else {
                Log.d("response Code", resp.code().toString())
                Log.d("response Type", resp.errorBody()!!.javaClass.name)
                Log.d("response Message", resp.errorBody()!!.string())
                errorMessages.add(resp.errorBody()!!.string())
                return UserError(errorMessages)
            }
        } catch (e: SocketTimeoutException) {
            Log.d("TimeOutException Maybe Server Closed", e.toString())
            return e
        } catch (e: EOFException) {
            Log.d("EOFException Maybe Response Data Type Mismatch", e.toString())
            return e
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
            return e
        }
    }

}
