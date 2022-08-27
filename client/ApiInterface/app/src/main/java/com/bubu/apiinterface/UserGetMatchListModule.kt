package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.net.SocketTimeoutException

data class UserGetMatchListResponseData(val list : List<UserGetMatchLists>)

data class UserGetMatchLists(
    @SerializedName("id") val id : String,
    @SerializedName("title") val title : String
)

class UserGetMatchListModule(override val userData: JsonObject)
    : UserApiInterface<UserGetMatchListResponseData> {

    interface UserGetMatchListInterface {
        @GET("/v1/matching/")
        fun get(
            //@Query("token") token : String
            //@Body body: JsonObject
        ): Call<UserGetMatchListResponseData>
        //보내는 데이터 형식
    }
    override suspend fun getApiData(): UserGetMatchListResponseData? {
        val retrofit = ApiClient.getApiClient()
        val retrofitObject = retrofit.create(UserGetMatchListInterface::class.java)
        try {
            var resp = retrofitObject.get().execute()
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