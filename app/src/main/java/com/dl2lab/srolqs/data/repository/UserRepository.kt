package com.dl2lab.srolqs.data.repository

import com.dl2lab.srolqs.data.preference.user.User
import com.dl2lab.srolqs.data.preference.user.UserPreference
import com.dl2lab.srolqs.data.remote.request.JoinClassRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.retrofit.ApiServiceSecured
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: User) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<User> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(userPreference: UserPreference): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference) }.also { instance = it }
    }
}