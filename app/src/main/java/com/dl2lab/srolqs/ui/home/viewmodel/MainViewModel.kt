package com.dl2lab.srolqs.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.dl2lab.srolqs.data.preference.user.User
import com.dl2lab.srolqs.data.remote.request.JoinClassRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.ListClassResponse
import com.dl2lab.srolqs.data.remote.retrofit.ApiConfig
import com.dl2lab.srolqs.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _token = MutableLiveData<String?>()
    val token: MutableLiveData<String?> = _token

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    fun getSession(): LiveData<User> {
        return userRepository.getSession().asLiveData()
    }
    fun getSessionWithToken(): LiveData<User> {
        return userRepository.getSession().asLiveData().also { userData ->
            userData.observeForever { user ->
                _token.value = user?.token
            }
        }
    }

    fun getToken(): LiveData<User> {
        val sessionLiveData = userRepository.getSession().asLiveData()
        sessionLiveData.observeForever { user ->
            _token.postValue(user?.token)
        }
        return sessionLiveData
    }


    fun joinClass(
        class_id: String
    ): LiveData<Response<BasicResponse>> = liveData {
        val responseLiveData = MutableLiveData<Response<BasicResponse>>()
        _isLoading.value = true
        val request = JoinClassRequest(class_id)
        val token = _token.value ?: ""
        val client = ApiConfig.getApiServiceSecured(token).joinClass(request)
        client.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                _isLoading.value = false
                responseLiveData.value = response
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                _isLoading.value = false
                val errorBody = (t.message ?: "Unknown error").toResponseBody(null)
                val errorResponse = Response.error<BasicResponse>(500, errorBody)
                responseLiveData.value = errorResponse
                _errorMessage.value = t.message
            }
        })
        emitSource(responseLiveData)
    }



    fun getListClass(): LiveData<Response<ListClassResponse>> = liveData {
        val responseLiveData = MutableLiveData<Response<ListClassResponse>>()
        _isLoading.value = true
        val token = _token.value ?: ""
        val client = ApiConfig.getApiServiceSecured(token).getListClass()
        client.enqueue(object : Callback<ListClassResponse> {
            override fun onResponse(
                call: Call<ListClassResponse>,
                response: Response<ListClassResponse>
            ) {
                _isLoading.value = false
                responseLiveData.value = response
            }

            override fun onFailure(call: Call<ListClassResponse>, t: Throwable) {
                _isLoading.value = false
                val errorBody = (t.message ?: "Unknown error").toResponseBody(null)
                val errorResponse = Response.error<ListClassResponse>(500, errorBody)
                responseLiveData.value = errorResponse
                _errorMessage.value = t.message
            }
        })
        emitSource(responseLiveData)
    }


    fun logout() {
        runBlocking {
            userRepository.logout()
        }
    }

    fun saveSession(userModel: User) {
        runBlocking {
            userRepository.saveSession(userModel)
        }
    }


}