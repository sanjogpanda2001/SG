package com.studgenie.app.data.remote.request

import com.studgenie.app.data.model.SendNumber
import com.studgenie.app.data.model.SendUserDetails
import com.studgenie.app.data.remote.response.UserDetailsApiResponse
import com.studgenie.app.data.remote.response.SignUpApiResponse
import com.studgenie.app.data.remote.response.SigninApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {
    @POST("/students/signup")
    fun userSignup(@Body number: SendNumber): Call<SignUpApiResponse>

    @POST("/students/createdetails")
    fun userDetails(@Body details: SendUserDetails): Call<List<UserDetailsApiResponse>>

    @POST("/students/otpSignIn")
    fun userSignin(@Body number: SendNumber): Call<SigninApiResponse>

}