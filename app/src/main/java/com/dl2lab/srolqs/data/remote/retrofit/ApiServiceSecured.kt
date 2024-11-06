package com.dl2lab.srolqs.data.remote.retrofit

import com.dl2lab.srolqs.data.remote.request.ChangePasswordRequest
import com.dl2lab.srolqs.data.remote.request.GetQuestionnaireRequest
import com.dl2lab.srolqs.data.remote.request.JoinClassRequest
import com.dl2lab.srolqs.data.remote.request.SubmitQuestionnaireRequest
import com.dl2lab.srolqs.data.remote.request.TambahKegiatanRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.DetailClassResponse
import com.dl2lab.srolqs.data.remote.response.ListClassResponse
import com.dl2lab.srolqs.data.remote.response.SubmitQuestionnaireResponse
import com.dl2lab.srolqs.data.remote.response.GetKegiatanResponse
import com.dl2lab.srolqs.data.remote.response.GetQuestionnaireResponse
import com.dl2lab.srolqs.data.remote.response.TambahKegiatanResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @POST("class/join")
    fun joinClass (
        @Body joinClasRequest: JoinClassRequest
    ): Call<BasicResponse>

    @GET("class/list")
    fun getListClass(): Call<ListClassResponse>

    @GET("class/detail/{id}")
    fun getClassDetail(
        @Path("id") id: String
    ): Call<DetailClassResponse>


    @GET("class/detail-class/{id}")
    fun getClassInformation(
        @Path("id") id: String
    ): Call<DetailClassResponse>


    @POST("questionnaire/submit")
    fun submitQuestionnaire(
        @Body submitQuestionnaireRequest: SubmitQuestionnaireRequest
    ): Call<SubmitQuestionnaireResponse>

    @GET("questionnaire/result")
    fun getQuestionnaireResult(
        @Query("class_id") classId: String,
        @Query("period") period: String
    ): Call<GetQuestionnaireResponse>
}