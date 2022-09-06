package com.bubu.apiinterface

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.EOFException
import java.net.SocketTimeoutException

data class UserGetCommunityListResponseData(@SerializedName("postList") val postList: List<PostList>)

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

class UserGetCommunityListModule(override val userData: Any?) : UserApiInterface {

    interface UserGetCommunityListInterface {
        //@Headers("Content-Type: application/json")
        @GET("/v1/community/")
        fun get(
            @Query("token") token : String
        ): Call<Any>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): Any? {
        try {
            var auth = UserAuthModule(null)
            val result = auth.getApiData()
            if (result == true) {
                val retrofit = ApiClient.getApiClient()
                val retrofitObject = retrofit.create(UserGetCommunityListInterface::class.java)
                try {
                    var resp = retrofitObject.get(userInformation.accessToken).execute()
                    if (resp.code() in 100..199) {
                        return super.handle100(resp)
                    } else if (resp.code() in 200..299) {
                        val responseBody = super.handle200(resp)
                        val jsonString: String =
                            Gson().toJsonTree(responseBody).asJsonObject.toString()
                        return convertToClass(
                            jsonString,
                            UserGetCommunityListResponseData::class.java
                        )
                    } else if (resp.code() in 300..399) {
                        return super.handle300(resp)
                    } else if (resp.code() in 400..499) {
                        return super.handle400(resp)
                    } else {
                        return super.handle500(resp)
                    }
                } catch (e: SocketTimeoutException) {
                    Log.d("TimeOutException", e.toString())
                    return e
                } catch (e: Exception) {
                    Log.d("Exception", e.toString())
                    return e
                } catch (e: EOFException) {
                    Log.d("EOFException Maybe Response Data Type Mismatch", e.toString())
                    return e
                }
            } else if (result is UserError) {
                //Auth Error Handling
                Log.d("error", "AuthError")
                return result
            } else if (result is UninitializedPropertyAccessException) {
                //Auth Error Handling
                Log.d("Exception", "AuthError")
                return result
            } else if (result is SocketTimeoutException) {
                //Auth Error Handling
                Log.d("Exception", "AuthError")
                return result
            } else if (result is EOFException) {
                //Auth Error Handling
                Log.d("Exception", "AuthError")
                return result
            } else if (result is Exception) {
                //Auth Error Handling
                Log.d("Exception", "AuthError")
                return result
            } else {
                //What is This ?
                Log.d("Exception", "What is this? ${result.toString()}")
                return result
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