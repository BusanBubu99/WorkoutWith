package com.bubu.interfacetest

import android.util.Log
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET


/*interface UserRegister { //인터페이스
    @FormUrlEncoded
    @GET("/v1/test") //요청 방식

    fun get(@Field("id") id:String, @Field("pw") password: String, @Field("email") email : String) : Call<UserRegisterResponseData>
    //보내는 데이터 형식
}

data class UserRegisterData(
    val id: String,
    val password: String,
    val email: String
) //실제 보낼 데이터

data class UserRegisterResponseData(
    val res : Int
)//응답 받은 데이터*/



/*fun main(){
    val retrofit = Retrofit.Builder()
    .baseUrl("http://3.34.141.115") // 주소는 본인의 서버 주소로 설정
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    val service = retrofit.create(UserRegister::class.java)//인터페이스를 적어줘야함
    val userData = UserRegisterData("textView1.text","textView2.text","textView3.text")

    service.get(userData.id, userData.email, userData.password).enqueue(object : Callback<UserRegisterResponseData> {
        override fun onResponse(call: Call<UserRegisterResponseData>, response: Response<UserRegisterResponseData>) {
            val result = response.body()
            Log.d("로그인", "${result?.res}")
        }

        override fun onFailure(call: Call<UserRegisterResponseData>, t: Throwable) {
            Log.e("로그인", "${t.localizedMessage}")
        }
    })
}*/







/*data class UserLoginData(
    @SerializedName("id") var id: String,
    @SerializedName("pw") var password: String
)

data class userInformation(val token : String, val id : String)

data class LogOutData(
    //서버랑 이야기
)

data class FindEmailSendData(
    @SerializedName("email") var email: String
)

data class FindEmailAuthData(
    @SerializedName("email") var email: String,
    @SerializedName("code") var code: String,
)

data class ChangePasswordData(
    @SerializedName("token") var token: String,
    @SerializedName("pw") val password: String,
)

data class GerProfileData(
    @SerializedName("targetId") val targetId: String,
)

data class UserProfileData(
    val name : String,
    val profilePic : file,
    val tags : String,
    val feeds: List<ReceivedSnsData>
)

data class CreateProfileData(
    @SerializedName("token") val token: String,
    @SerializedName("name") val password: String,
    @SerializedName("profilePic") val profilePic: /*서버랑 정해야함*/file,
    @SerializedName("tags") val tags: String
)

data class WriteSnsData(
    @SerializedName("token") val token: String,
    @SerializedName("comment") val comment: String,
    @SerializedName("timestamp") val timestamp: String, //시간형식으로 변환해서
    @SerializedName("like") val id: obj, //
    @SerializedName("memberMax") val memberMax: Int,
    @SerializedName("game") val game: Int //운동 종목 코드
)

data class GetFeedData(
    @SerializedName("token") val token: String,
    @SerializedName("count") val count: Int,
)

data class ReceivedSnsData(
    val id : String,
    val name : String,
    val content: String,
    val picture: file, //
    val timestamp: String, //시간형식
    val like: obj,//
    val memberMax: Int,
    val game: Int,
)

data class CreateTeamData(
    @SerializedName("groupName") val groupName: String,
    @SerializedName("groupMember") val groupMember: obj //id 배열
)

data class AddFriendData(
    @SerializedName("token") val token: String,
    @SerializedName("targetId") val targetId: String
)

data class DeleteFriendData(
    @SerializedName("token") val token: String,
    @SerializedName("targetId") val targetId: String,
)

data class ReceivedNearMatchingData(
    val location : obj,
    val time : obj , //가능한 시작시간, 가능한 끝나는시간
    val game : Int //게임 코드
)

data class AutoMatchingData(
    @SerializedName("location") val location : obj,
    @SerializedName("searchRange") val searchRange : Float,
    @SerializedName("game") val game : Int,
    @SerializedName("time") val time : Obj //가능한 시작시간, 가능한 끝나는 시간
)

data class ReceivedAutoMatchingData(
    val queueId : String,
    val memberCount : Int
)

/*data class SetGoal(

)*/

data class SetLocation(
    @SerializedName("position") val position : obj //위치 객체
)*/
