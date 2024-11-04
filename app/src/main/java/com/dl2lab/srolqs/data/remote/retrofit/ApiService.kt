package com.dl2lab.srolqs.data.remote.retrofit

import com.dl2lab.srolqs.data.remote.request.CreateNewPasswordRequest
import com.dl2lab.srolqs.data.remote.request.ForgotPasswordCodeRequest
import com.dl2lab.srolqs.data.remote.request.LoginRequest
import com.dl2lab.srolqs.data.remote.request.RegisterRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.LoginResponse
import com.dl2lab.srolqs.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("user/register")
    fun register (
        @Body registerRequest: RegisterRequest
    ): Call<RegisterResponse>

    @POST("user/login")
    fun login (
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @POST("user/request-reset-password")
    fun sendResetPasswordCode (
        @Body request: ForgotPasswordCodeRequest
    ): Call<BasicResponse>

    @POST("user/reset-password")
    fun createNewPassword (
        @Body request: CreateNewPasswordRequest
    ):Call<BasicResponse>

}