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

data class UserGetRoomVoteResponseData(
    @SerializedName("voteTitle") val voteTitle: String,
    @SerializedName("time") val time: voteTimeObject,
    @SerializedName("content") val content: String,
    @SerializedName("userList") val userList: List<userListObject>,
)

data class voteTimeObject(
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("date") val date: String
)

data class userListObject(
    @SerializedName("userId") val userId : String,
    @SerializedName("profilePic") val profilePic : String,//file
    @SerializedName("like") val like : Int
)

//response
//{
//    voteTitle: 제목,
//    time : {
//    starttime: 시작 시간
//    endtime: 끝 시간,
//    date: 날짜
//}
//    content: 투표 내용,
//    userlist : [
//    {
//        userid: 사용자id
//        profilePic : 프로필 사진
//        like : 좋아요 수
//    }
//    ]
//}
// //
class UserGetRoomVoteModule(override val token: String, override val userData: JsonObject)
    : UserApiInterface<JsonObject, UserGetRoomVoteResponseData> {

    interface UserGetRoomVoteInterface {
        //@Headers("Content-Type: application/json")
        @GET("/v1/matching/vote/")
        fun get(
            @Query("voteId") voteId : JsonObject
            //@Body body: JsonObject
        ): Call<UserGetRoomVoteResponseData>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): UserGetRoomVoteResponseData? {
        val retrofit = Retrofit.Builder()
            .baseUrl(super.serverAddress)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitObject = retrofit.create(UserGetRoomVoteInterface::class.java)
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