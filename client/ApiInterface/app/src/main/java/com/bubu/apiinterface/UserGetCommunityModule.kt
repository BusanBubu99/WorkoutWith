package com.bubu.apiinterface

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.SocketTimeoutException

data class UserGetCommunityResponseData(
    @SerializedName("userId") val userId : String,
    @SerializedName("title") val title : String,
    @SerializedName("picture") val picture : String,
    @SerializedName("timestamp") val timestamp : String,
    @SerializedName("content") val content : String,
    @SerializedName("like") val like : List<LikeObj>
)

data class LikeObj(
    @SerializedName("userId") val userId : String
)
//userId: 사용자 id
//title : 게시글 제목
//picture : 게시글 사진
//timestamp : 게시글 작성 날짜
//content : 게시글 내용
//l//ike : {
//    [
//        userId: 좋아요 누른 사람의 id
//    ]
//}

class UserGetCommunityModule(override val userData: JsonObject)
    : UserApiInterface {
    interface UserGetCommunityInterface {
        //@Headers("Content-Type: application/json")
        @GET("/v1/community/")
        fun get(
            @Query("postId") postId : String
            //@Body body: JsonObject
        ): Call<UserGetCommunityResponseData>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): UserGetCommunityResponseData? {
        val retrofit = ApiClient.getApiClient()
        val retrofitObject = retrofit.create(UserGetCommunityInterface::class.java)
        try {
            var resp = retrofitObject.get(userData["postId"].toString()).execute()
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