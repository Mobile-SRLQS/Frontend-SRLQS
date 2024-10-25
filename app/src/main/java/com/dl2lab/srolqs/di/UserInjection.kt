package com.dl2lab.srolqs.di

import android.content.Context
import com.dl2lab.srolqs.data.preference.user.UserPreference
import com.dl2lab.srolqs.data.preference.user.dataStore
import com.dl2lab.srolqs.data.repository.UserRepository

object UserInjection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}