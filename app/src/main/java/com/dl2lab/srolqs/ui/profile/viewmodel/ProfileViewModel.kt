package com.dl2lab.srolqs.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.dl2lab.srolqs.data.preference.user.User
import com.dl2lab.srolqs.data.remote.request.ChangePasswordRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.retrofit.ApiConfig
import com.dl2lab.srolqs.data.repository.SecuredRepository
import com.dl2lab.srolqs.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response


class ProfileViewModel(
    private val repository: SecuredRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _token = MutableLiveData<String?>()
    val token: MutableLiveData<String?> = _token

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _changePasswordData = MutableLiveData<BasicResponse>()
    val changePasswordData: LiveData<BasicResponse> = _changePasswordData

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData().also { userData ->
            userData.observeForever { user ->
                _token.value = user?.token
            }
        }
    }


    fun logout() {
        runBlocking {
            repository.logout()
        }
    }

    fun saveSession(userModel: User) {
        runBlocking {
            repository.saveSession(userModel)
        }
    }

    fun changePassword(
        oldPassword: String, newPassword: String, confirmedPassword: String
    ): LiveData<Response<BasicResponse>> = liveData {
        val responseLiveData = MutableLiveData<Response<BasicResponse>>()
        _isLoading.value = true
        val request = ChangePasswordRequest(oldPassword, newPassword, confirmedPassword)
        val token = _token.value ?: ""
        val client = ApiConfig.getApiServiceSecured(token).changePassword(request)
        client.enqueue(object : retrofit2.Callback<BasicResponse> {
            override fun onResponse(
                call: retrofit2.Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                _isLoading.value = false
                responseLiveData.value = response
            }

            override fun onFailure(call: retrofit2.Call<BasicResponse>, t: Throwable) {
                _isLoading.value = false
                val errorBody = (t.message ?: "Unknown error").toResponseBody(null)
                val errorResponse = Response.error<BasicResponse>(500, errorBody)
                responseLiveData.value = errorResponse
                _errorMessage.value = t.message
            }
        })
        emitSource(responseLiveData)
    }
}