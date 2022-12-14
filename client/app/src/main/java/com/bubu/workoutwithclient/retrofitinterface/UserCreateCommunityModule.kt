package com.bubu.workoutwithclient.retrofitinterface

import android.util.Log
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.EOFException
import java.io.File
import java.net.SocketTimeoutException

/**
 * UserCreateCommunityModule
 * create community posts
 *
 * Parameter : UserChangePasswordData
 * shown below
 *
 *
 * Return Value : UserError / Http Response Code
 * If communication is successful Response Code
 * else UserError
 *
 * Exception :
 * SocketTimeOutException : if Server is closed
 * EOFException : Response Type Mismatch
 * Exception :
 * Exceptions we don't know yet
 * */

data class UserCreateCommunityData(
    val title: String, val picture: File?/*absolute File Path*/,
    val content: String
)

class UserCreateCommunityModule(override val userData: UserCreateCommunityData) : UserApiInterface {

    interface UserCreateCommunityInterface {
        @Multipart
        @POST("/v1/community/")
        fun get(
            @Part("title") name: String,
            @Part file: MultipartBody.Part?,
            @Part("contents") content: String,
        ): Call<Any>
        //보내는 데이터 형식
    }

    interface UserCreateCommunityNoPictureInterface : UserCreateCommunityInterface {
        @Headers("Content-Type: application/json")
        @POST("/v1/community/")
        fun get(
            @Body body: JsonObject
        ): Call<Any>
    }

    override suspend fun getApiData(): Any? {
        try {
            var auth = UserAuthModule()
            val result = auth.getApiData()
            val retrofit = ApiTokenHeaderClient.getApiClient()
            if (result == true) {
                Log.d("userInformation in create", userInformation.accessToken)
                if (userData.picture == null) {
                    var retrofitObject =
                        retrofit.create(UserCreateCommunityNoPictureInterface::class.java)
                    try {
                        var requestBody = JsonObject()
                        requestBody.addProperty("title", userData.title)
                        requestBody.addProperty("contents", userData.content)
                        Log.d("re",userData.title)
                        Log.d("re2",userData.content)
                        var resp = retrofitObject.get(requestBody).execute()
                        if (resp.code() in 100..199) {
                            return super.handle100(resp)
                        } else if (resp.code() in 200..299) {
                            return resp.code()
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
                } else {
                    try {
                        var retrofitObject =
                            retrofit.create(UserCreateCommunityInterface::class.java)
                        val requestFile =
                            RequestBody.create(MediaType.parse("image/*"), userData.picture)
                        var profilePicture = MultipartBody.Part.createFormData(
                            "picture",
                            userData.picture.name,
                            requestFile
                        )
                        Log.d("re",userData.title)
                        Log.d("re2",userData.content)
                        var resp =
                            retrofitObject.get(userData.title, profilePicture, userData.content)
                                .execute()
                        if (resp.code() in 100..199) {
                            return super.handle100(resp)
                        } else if (resp.code() in 200..299) {
                            return resp.code()
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