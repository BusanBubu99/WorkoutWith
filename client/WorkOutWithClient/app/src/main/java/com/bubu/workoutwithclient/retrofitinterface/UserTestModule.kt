package com.bubu.workoutwithclient.retrofitinterface

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*
import java.net.SocketTimeoutException

data class UserTestResponseData(
    @SerializedName("_id") val id : String,
    @SerializedName("name") val name :String,
    @SerializedName("tags") val tags : String
)

class UserTestModule(override val userData: JsonObject) : UserApiInterface {

    interface UserTestInterface {
        @Headers("Content-Type: application/json")
        @POST("/v1/test/")
        fun get(
            //@Query("postId") postId : String
            @Body body: JsonObject
        ): Call<Any>
        //보내는 데이터 형식
    }
    override suspend fun getApiData(): Any? {
        val retrofit = ApiTokenHeaderClient.getApiClient()
        val retrofitObject = retrofit.create(UserTestInterface::class.java)
        try {
            var resp = retrofitObject.get(userData).execute()
            if(resp.body() == null) {
                return resp.errorBody()?.string()
            }
            if(resp.code() in 200..299) { //
                Log.d("response Code", resp.code().toString())
                Log.d("response", resp.body().toString())
                return resp.body()
                //return  List<UserTestResponseData>()
                //Parsing Here!
                //...
                //....
                return null
            } else if (resp.code() in 300 .. 399) {
                Log.d("response Code", resp.code().toString())
                return resp.body()
            } else {
                return resp.body()
            }
        } catch (e : SocketTimeoutException) {
            Log.d("TimeOutException@",e.toString())
            return e
        } catch (e : Exception) {
            Log.d("Exception@", e.toString())
            return e
        }
    }
}