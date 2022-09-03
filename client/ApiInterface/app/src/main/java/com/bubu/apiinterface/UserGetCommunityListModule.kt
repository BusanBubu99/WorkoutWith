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

data class UserGetCommunityListResponseData(@SerializedName("postList") val postList : List<PostList>)

data class PostList(
    @SerializedName("id") val id: String,
    @SerializedName("postId") val postId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("picture") val picture: String,//file
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("likeCount") val likeCount: Int
)
//postlist : [
//id : 게시글 작성자 id
//postid : 게시글 id
//title : 제목
//picture : 게시글 사진
//timestamp : 게시글 작성 시간
//likecount : 좋아요 수
//]

class UserGetCommunityListModule(override val userData: JsonObject)
    : UserApiInterface {

    interface UserGetCommunityListInterface {
        //@Headers("Content-Type: application/json")
        @GET("/v1/community/")
        fun get(
            //@Query("token") token : String
            //@Body body: JsonObject
        ): Call<UserGetCommunityListResponseData>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): UserGetCommunityListResponseData? {
        val retrofit = ApiClient.getApiClient()
        val retrofitObject = retrofit.create(UserGetCommunityListInterface::class.java)
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