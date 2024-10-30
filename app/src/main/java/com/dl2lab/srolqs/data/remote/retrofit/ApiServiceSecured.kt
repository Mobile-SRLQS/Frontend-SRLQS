package com.dl2lab.srolqs.data.remote.retrofit

import com.dl2lab.srolqs.data.remote.request.ChangePasswordRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface ApiServiceSecured {

    @PUT("user/update-password")
    fun changePassword (
        @Body changePasswordRequest: ChangePasswordRequest
    ): Call<BasicResponse>
}