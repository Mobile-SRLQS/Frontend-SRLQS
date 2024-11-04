package com.dl2lab.srolqs.data.remote.retrofit

import com.dl2lab.srolqs.data.remote.request.ChangePasswordRequest
import com.dl2lab.srolqs.data.remote.request.TambahKegiatanRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.GetKegiatanResponse
import com.dl2lab.srolqs.data.remote.response.TambahKegiatanResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiServiceSecured {

    @PUT("user/update-password")
    fun changePassword (
        @Body changePasswordRequest: ChangePasswordRequest
    ): Call<BasicResponse>

    @POST("todo/create")
    fun tambahKegiatan (
        @Body tambahKegiatanRequest: TambahKegiatanRequest
    ): Call<TambahKegiatanResponse>

    @GET("todo/list")
    fun getListKegiatan(): Call<GetKegiatanResponse>
}