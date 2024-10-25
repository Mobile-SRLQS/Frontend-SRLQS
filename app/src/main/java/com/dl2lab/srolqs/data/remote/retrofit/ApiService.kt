package com.dl2lab.srolqs.data.remote.retrofit

import com.dl2lab.srolqs.data.remote.request.RegisterRequest
import com.dl2lab.srolqs.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("user/register")
    fun register (
        @Body registerRequest: RegisterRequest
    ): Call<RegisterResponse>
}