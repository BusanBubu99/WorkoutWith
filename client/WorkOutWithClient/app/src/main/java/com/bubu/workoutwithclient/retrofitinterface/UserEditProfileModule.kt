package com.bubu.workoutwithclient.retrofitinterface

import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.EOFException
import java.io.File
import java.net.SocketTimeoutException

/**
 * UserEditProfileModule
 * Edit user's profile
 *
 * Parameter : UserEditProfileData
 * shown below
 *
 *
 * Return Value : UserError / UserEditProfileResponseData
 * If communication is successful UserEditProfileResponseData that is defined below
 * else UserError
 *
 * Exception :
 * SocketTimeOutException : if Server is closed
 * EOFException : Response Type Mismatch
 * Exception :
 * Exceptions we don't know yet
 * */

data class UserEditProfileData(
    val name: String,
    val profilePic: File, //Must be File
    val tags: String,
    val city: String,
    val county: String,
    val district: String
)

data class UserEditProfileResponseData(
    @SerializedName("userid") val userId : String,
    @SerializedName("name") val name: String,
    @SerializedName("profilePic") val profilePic: String,
    @SerializedName("tags") val tags: String,
    @SerializedName("city") val city: String,
    @SerializedName("county") val county: String,
    @SerializedName("district") val district: String
)


class UserEditProfileModule(override val userData: UserEditProfileData) : UserApiInterface {

    interface UserEditProfileInterface {
        @Multipart
        @POST("/v1/profile")
        fun get(
            @Part("name") name: String,
            @Part file: MultipartBody.Part?,
            @Part("tags") tags: String,
            @Part("city") city: String,
            @Part("county") county: String,
            @Part("district") district: String
        ): Call<Any>
        //보내는 데이터 형식
    }

    override suspend fun getApiData(): Any? {
        try {
            var auth = UserAuthModule()
            val result = auth.getApiData()
            if (result == true) {

                val requestFile =
                    RequestBody.create(MediaType.parse("image/*"), userData.profilePic)
                val body = MultipartBody.Part.createFormData(
                    "profilePic",
                    userInformation.userId + userData.profilePic.name,
                    requestFile
                )

                //Do Any Operation or Jobs..
                val retrofit = ApiTokenHeaderClient.getApiClient()
                val retrofitObject = retrofit.create(UserEditProfileInterface::class.java)
                var resp = retrofitObject.get(
                    userData.name, body, userData.tags, userData.city, userData.county,
                    userData.district
                ).execute()
                if (resp.code() in 100..199) {
                    return super.handle100(resp)
                } else if (resp.code() in 200..299) {
                    val responseBody = super.handle200(resp)
                    val jsonString: String = Gson().toJsonTree(responseBody).asJsonObject.toString()
                    Log.d("jsonString", jsonString)
                    Log.d("After jsonString",jsonString.replace("\"",""))
                    return convertToClass(jsonString, UserEditProfileResponseData::class.java)
                } else if (resp.code() in 300..399) {
                    return super.handle300(resp)
                } else if (resp.code() in 400..499) {
                    return super.handle400(resp)
                } else {
                    return super.handle500(resp)
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