package com.dl2lab.srolqs.data.repository

import com.dl2lab.srolqs.data.preference.user.User
import com.dl2lab.srolqs.data.preference.user.UserPreference
import com.dl2lab.srolqs.data.remote.request.GetQuestionnaireRequest
import com.dl2lab.srolqs.data.remote.request.JoinClassRequest
import com.dl2lab.srolqs.data.remote.request.SubmitQuestionnaireRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.DetailClassResponse
import com.dl2lab.srolqs.data.remote.response.GetKegiatanResponse
import com.dl2lab.srolqs.data.remote.response.GetProfileResponse
import com.dl2lab.srolqs.data.remote.response.GetQuestionnaireResponse
import com.dl2lab.srolqs.data.remote.response.JoinDetailClassResponse
import com.dl2lab.srolqs.data.remote.response.ListClassResponse
import com.dl2lab.srolqs.data.remote.response.ShowAvailablePeriodResponse
import com.dl2lab.srolqs.data.remote.response.StudentProgressResponse
import com.dl2lab.srolqs.data.remote.response.SubmitQuestionnaireResponse
import com.dl2lab.srolqs.data.remote.retrofit.ApiConfig.getApiServiceSecured
import com.dl2lab.srolqs.data.remote.retrofit.ApiServiceSecured
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call

class SecuredRepository private constructor(
    private val userPreference: UserPreference
) {

    private lateinit var apiServiceSecured: ApiServiceSecured
    fun getSession(): Flow<User> {
        var session = userPreference.getSession()
        runBlocking {
            apiServiceSecured = session.first().token?.let { getApiServiceSecured(it) }!!
        }
        return session
    }


    suspend fun saveSession(user: User) {
        runBlocking {
            userPreference.saveSession(user)
        }
        apiServiceSecured = user.token?.let { getApiServiceSecured(it) }!!

    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun joinClass(objectDTO: JoinClassRequest) : Call<BasicResponse> {
        return apiServiceSecured.joinClass(objectDTO)

    }

    fun getListClass() : Call<ListClassResponse>{
        return apiServiceSecured.getListClass()

    }

    fun getDetailClass(id: String) : Call<JoinDetailClassResponse>{
        return apiServiceSecured.getClassDetail(id)
    }

    fun getClassInformation(id: String) : Call<DetailClassResponse>{
        return apiServiceSecured.getClassInformation(id)
    }

    fun submitQuestionnaire(objectDTO: SubmitQuestionnaireRequest) : Call<SubmitQuestionnaireResponse> {
        return apiServiceSecured.submitQuestionnaire(objectDTO)
    }

    fun getQuestionnaire(classId: String, period: String): Call<GetQuestionnaireResponse> {
        return apiServiceSecured.getQuestionnaireResult(classId, period)
    }

    fun getAvailablePeriod(classId : String) : Call<ShowAvailablePeriodResponse>{
        return apiServiceSecured.getAvailablePeriod(classId)
    }

    fun getStudentProgress(classId : String, type: String) : Call<StudentProgressResponse> {
        return apiServiceSecured.getStudentProgress(classId, type)
    }

    fun getListKegiatan() : Call<GetKegiatanResponse>{
        return apiServiceSecured.getListKegiatan()
    }

    fun getListKegiatanByType(type: String) : Call<GetKegiatanResponse>{
        return apiServiceSecured.getKegiatanByType(type)
    }

    fun checklistKegiatan(id: Int) : Call<BasicResponse>{
        return apiServiceSecured.checklistKegiatan(id)
    }

    fun deleteKegiatan(id: Int) : Call<BasicResponse>{
        return apiServiceSecured.deleteKegaiatan(id)
    }

    fun getDetailProfile() : Call<GetProfileResponse>{
        return apiServiceSecured.getProfileDetail()
    }



        companion object {
        @Volatile
        private var instance: SecuredRepository? = null
        fun getInstance(
            userPreference: UserPreference,
        ): SecuredRepository =
            instance ?: synchronized(this) {
                instance ?: SecuredRepository(userPreference)
            }.also { instance = it }
    }
}
