package com.dl2lab.srolqs.data.remote.retrofit

import com.dl2lab.srolqs.data.remote.request.ChangePasswordRequest
import com.dl2lab.srolqs.data.remote.request.GetQuestionnaireRequest
import com.dl2lab.srolqs.data.remote.request.JoinClassRequest
import com.dl2lab.srolqs.data.remote.request.SubmitQuestionnaireRequest
import com.dl2lab.srolqs.data.remote.request.TambahKegiatanRequest
import com.dl2lab.srolqs.data.remote.request.UpdateKegiatanRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.DetailClassResponse
import com.dl2lab.srolqs.data.remote.response.EditProfileResponse
import com.dl2lab.srolqs.data.remote.response.GetKegiatanDetailResponse
import com.dl2lab.srolqs.data.remote.response.ListClassResponse
import com.dl2lab.srolqs.data.remote.response.SubmitQuestionnaireResponse
import com.dl2lab.srolqs.data.remote.response.GetKegiatanResponse
import com.dl2lab.srolqs.data.remote.response.GetProfileResponse
import com.dl2lab.srolqs.data.remote.response.GetQuestionnaireResponse
import com.dl2lab.srolqs.data.remote.response.KegiatanItem
import com.dl2lab.srolqs.data.remote.response.ShowAvailablePeriodResponse
import com.dl2lab.srolqs.data.remote.response.StudentProgressResponse
import com.dl2lab.srolqs.data.remote.response.TambahKegiatanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @GET("class/available-periods/{class_id}")
    fun getAvailablePeriod(
        @Path("class_id") classId :String
    ) : Call<ShowAvailablePeriodResponse>

    @GET("questionnaire/progress/dimensi")
    fun getStudentProgress(
        @Query("class_id") classId :String,
        @Query("type") type: String
    ) : Call<StudentProgressResponse>

    @POST("questionnaire/submit")
    fun submitQuestionnaire(
        @Body submitQuestionnaireRequest: SubmitQuestionnaireRequest
    ): Call<SubmitQuestionnaireResponse>

    @GET("questionnaire/result")
    fun getQuestionnaireResult(
        @Query("class_id") classId: String,
        @Query("period") period: String
    ): Call<GetQuestionnaireResponse>

    @DELETE("todo/{id}")
    fun checklistKegiatan(
        @Path("id") id: Int
    ): Call<BasicResponse>

    @GET("todo/list/{type}")
    fun getKegiatanByType(
        @Path("type") type: String
    ): Call<GetKegiatanResponse>

    @GET("todo/{id}")
    fun getKegiatanDetail(
        @Path("id") id: Int
    ): Call<GetKegiatanDetailResponse>

    @PUT("todo/{id}")
    fun updateKegiatan(
        @Path("id") id: Int,
        @Body updateKegiatanRequest: UpdateKegiatanRequest
    ): Call<BasicResponse>

    @Multipart
    @PUT("user/update")
    fun editProfile(
        @Part("nama") nama: RequestBody,
        @Part("birth_date") birthDate: RequestBody,
        @Part("institution") institution: RequestBody,
        @Part("degree") degree: RequestBody,
        @Part("identity_number") identityNumber: RequestBody,
        @Part("batch") batch: RequestBody,
        @Part profilePicture: MultipartBody.Part? // Optional, if no profile picture update
    ): Call<EditProfileResponse>

    @GET("user/detail-user")
    fun getProfileDetail(
    ): Call<GetProfileResponse>

}