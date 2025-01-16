package com.dl2lab.srolqs.data.preference.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define the DataStore instance
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    // Function to save user session data
    suspend fun saveSession(user: User) {
        dataStore.edit { preferences ->
            preferences[ID] = user.id.toString()
            preferences[NAMA] = user.nama
            preferences[BIRTH_DATE] = user.birthDate
            preferences[EMAIL] = user.email
            preferences[PASSWORD] = user.password
            preferences[IDENTITY_NUMBER] = user.identityNumber
            preferences[BATCH] = user.batch
            preferences[INSTITUTION] = user.institution
            preferences[DEGREE] = user.degree
            preferences[ROLE] = user.role
            preferences[RESET_CODE] = user.resetCode.toString()
            preferences[RESET_CODE_EXPIRY] = user.resetCodeExpiry.toString()
            preferences[TOKEN] = user.token ?: ""
            preferences[PROFILE_PICTURE] = user.profilePicture
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN]
        }
    }

    // Function to retrieve user session data
    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                id = preferences[ID]?.toInt() ?: 0,
                nama = preferences[NAMA] ?: "",
                birthDate = preferences[BIRTH_DATE] ?: "",
                email = preferences[EMAIL] ?: "",
                password = preferences[PASSWORD] ?: "",
                identityNumber = preferences[IDENTITY_NUMBER] ?: ""  ,
                batch = preferences[BATCH] ?: "",
                institution = preferences[INSTITUTION] ?: "",
                degree = preferences[DEGREE] ?: "",
                role = preferences[ROLE] ?: "",
                resetCode = preferences[RESET_CODE] ?: "",
                resetCodeExpiry = preferences[RESET_CODE_EXPIRY] ?: "",
                token = preferences[TOKEN] ?: "",
                profilePicture = preferences[PROFILE_PICTURE] ?: "",
                isLogin = preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    // Function to clear user session data (e.g., on logout)
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        // Define keys for each user attribute
        private val ID = stringPreferencesKey("id")
        private val NAMA = stringPreferencesKey("nama")
        private val BIRTH_DATE = stringPreferencesKey("birth_date")
        private val EMAIL = stringPreferencesKey("email")
        private val PASSWORD = stringPreferencesKey("password")
        private val IDENTITY_NUMBER = stringPreferencesKey("identity_number")
        private val BATCH = stringPreferencesKey("batch")
        private val INSTITUTION = stringPreferencesKey("institution")
        private val DEGREE = stringPreferencesKey("degree")
        private val ROLE = stringPreferencesKey("role")
        private val TOKEN = stringPreferencesKey("token")
        private val RESET_CODE = stringPreferencesKey("reset_code")
        private val RESET_CODE_EXPIRY = stringPreferencesKey("reset_code_expiry")
        private val PROFILE_PICTURE = stringPreferencesKey("profile_picture")
        private val IS_LOGIN_KEY = booleanPreferencesKey("is_login")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}