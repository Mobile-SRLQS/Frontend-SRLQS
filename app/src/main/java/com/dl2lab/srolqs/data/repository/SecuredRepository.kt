package com.dl2lab.srolqs.data.repository

import com.dl2lab.srolqs.data.preference.user.User
import com.dl2lab.srolqs.data.preference.user.UserPreference
import com.dl2lab.srolqs.data.remote.request.JoinClassRequest
import com.dl2lab.srolqs.data.remote.request.SubmitQuestionnaireRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.DetailClassResponse
import com.dl2lab.srolqs.data.remote.response.ListClassResponse
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

    fun getDetailClass(id: String) : Call<DetailClassResponse>{
        return apiServiceSecured.getClassDetail(id)
    }

    fun submitQuestionnaire(objectDTO: SubmitQuestionnaireRequest) : Call<SubmitQuestionnaireResponse> {
        return apiServiceSecured.submitQuestionnaire(objectDTO)
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
