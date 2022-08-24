package com.bubu.interfacetest

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

class UserNearMatchNotice {

    /*interface UserNearMatchNoticeInterface { //인터페이스
        @FormUrlEncoded
        @GET("/v1/test") //요청 방식

        fun get(@Field("token") token:String, @Field("targetId") targetId:String) : Call<UserNearMatchNoticeResponseData>
        //보내는 데이터 형식
    }*/

    data class UserNearMatchNoticeResponseData(
        @SerializedName("responseCode") override val responseCode: Int,
        @SerializedName("location") val location : obj, //위치 정보
        @SerializedName("timestamp") val timestamp : String, //운동 시간대
        @SerializedName("game") val game : Int//운동 종목
    ) : UserResponseData//응답 받은 데이터

    //TODO(Listening Function!)
}