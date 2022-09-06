package com.bubu.apiinterface

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.EOFException
import java.net.SocketTimeoutException


data class UserGetProfileData(val targetId: String)

data class UserGetProfileResponseData(
    @SerializedName("userId") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("profilePic") val profilePic: String,//file
    @SerializedName("tags") val tags: String,
    @SerializedName("city") val city : String,
    @SerializedName("county") val county : String,
    @SerializedName("district") val district : String,
    @SerializedName("communityPost") val communityPost: List<UserGetProfileCommunityPostResponseData>
)

data class UserGetProfileCommunityPostResponseData(
    @SerializedName("postUserId") val postUserId: String,
    @SerializedName("postId") val postId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("picture") val picture: String,//URL
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("like") val like: Int
)

class UserGetProfileModule(override val userData: UserGetProfileData) : UserApiInterface {

    interface UserGetProfileInterface {
        @GET("/v1/profile")
        fun get(
            @Query("token") accessToken : String,
            @Query("targetId") targetId: String
        ): Call<Any>
        //보내는 데이터 형식
    }


    override suspend fun getApiData(): Any? {
        try {
            var auth = UserAuthModule(null)
            val result = auth.getApiData()
            if (result == true) {
                //Do Any Operation or Jobs..
                val retrofit = ApiClient.getApiClient()
                val retrofitObject = retrofit.create(UserGetProfileInterface::class.java)
                try {
                    var resp = retrofitObject.get(userInformation.accessToken, userData.targetId).execute()
                    if (resp.code() in 100..199) {
                        return super.handle100(resp)
                    } else if (resp.code() in 200..299) {
                        val responseBody = super.handle200(resp)
                        val jsonString: String = Gson().toJsonTree(responseBody).asJsonObject.toString()
                        return convertToClass(jsonString, UserGetProfileResponseData::class.java)
                        /*val userId = jsonObject.get("userId").toString()
                        val name = jsonObject.get("name").toString()
                        val profilePic = jsonObject.get("profilePic").toString()//must be Edit
                        val tags = jsonObject.get("tags").toString()
                        val city = jsonObject.get("city").toString()
                        val county = jsonObject.get("county").toString()
                        val district = jsonObject.get("district").toString()
                        val communityPost = jsonObject.getAsJsonArray("communityPost")
                        val list = mutableListOf<UserGetProfileCommunityPostResponseData>()
                        communityPost.forEach {
                            //can be null..
                            list.add(
                                UserGetProfileCommunityPostResponseData(
                                    it.asJsonObject.get("postUserId").toString(),
                                    it.asJsonObject.get("postId").toString().toInt(),
                                    it.asJsonObject.get("title").toString(),
                                    it.asJsonObject.get("picture").toString(),
                                    it.asJsonObject.get("timestamp").toString(),
                                    it.asJsonObject.get("like").toString().toInt()
                                )
                            )
                        }
                        return UserGetProfileResponseData(userId,name,profilePic,tags,city,county,district,list)*/
                    } else if (resp.code() in 300..399) {
                        return super.handle300(resp)
                    } else if (resp.code() in 400..499) {
                        return super.handle400(resp)
                    } else {
                        return super.handle500(resp)
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
            } else if (result is UserError) {
                //Auth Error Handling
                return result
            } else if (result is UninitializedPropertyAccessException) {
                //Auth Error Handling
                return result
            } else if (result is SocketTimeoutException) {
                //Auth Error Handling
                return result
            } else if (result is EOFException) {
                //Auth Error Handling
                return result
            } else if (result is Exception) {
                //Auth Error Handling
                return result
            } else {
                //What is This ?
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