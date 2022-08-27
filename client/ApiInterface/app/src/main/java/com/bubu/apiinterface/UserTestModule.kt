package com.bubu.apiinterface

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

class UserTestModule(override val userData: JsonObject) : UserApiInterface<List<UserTestResponseData>> {

    interface UserTestInterface {
        @Headers("Content-Type: application/json")
        @POST("/v1/test/")
        fun get(
            //@Query("postId") postId : String
            @Body body: JsonObject
        ): Call<List<UserTestResponseData>>
        //보내는 데이터 형식
    }
    override suspend fun getApiData(): List<UserTestResponseData>? {
        val retrofit = ApiClient.getApiClient()
        val retrofitObject = retrofit.create(UserTestInterface::class.java)
        try {
            var resp = retrofitObject.get(userData).execute()
            if(resp.code() == OK) { //
                Log.d("response Code", resp.code().toString())
                Log.d("response", resp.body().toString())
                return resp.body()
                //return  List<UserTestResponseData>()
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