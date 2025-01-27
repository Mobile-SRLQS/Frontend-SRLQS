package com.dl2lab.srolqs.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.dl2lab.srolqs.data.preference.user.User
import com.dl2lab.srolqs.data.remote.request.JoinClassRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.DetailClassResponse
import com.dl2lab.srolqs.data.remote.response.GetKegiatanResponse
import com.dl2lab.srolqs.data.remote.response.JoinDetailClassResponse
import com.dl2lab.srolqs.data.remote.response.ListClassResponse
import com.dl2lab.srolqs.data.remote.retrofit.ApiConfig
import com.dl2lab.srolqs.data.repository.SecuredRepository
import com.dl2lab.srolqs.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val repository: SecuredRepository
) : ViewModel() {

    private val _token = MutableLiveData<String?>()
    val token: MutableLiveData<String?> = _token

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _loadingDetailClass = MutableLiveData<Boolean>()
    val loadingDetailClass: LiveData<Boolean> = _loadingDetailClass

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun joinClass(
        class_id: String
    ): LiveData<Response<BasicResponse>> = liveData {
        val responseLiveData = MutableLiveData<Response<BasicResponse>>()
        _isLoading.value = true
        val request = JoinClassRequest(class_id)
        val client = repository.joinClass(request)
        client.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>, response: Response<BasicResponse>
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
        val token = _token.value ?: ""
        val client = repository.getListClass()
        client.enqueue(object : Callback<ListClassResponse> {
            override fun onResponse(
                call: Call<ListClassResponse>, response: Response<ListClassResponse>
            ) {
                responseLiveData.value = response
            }

            override fun onFailure(call: Call<ListClassResponse>, t: Throwable) {
                val errorBody = (t.message ?: "Unknown error").toResponseBody(null)
                val errorResponse = Response.error<ListClassResponse>(500, errorBody)
                responseLiveData.value = errorResponse
                _errorMessage.value = t.message
            }
        })
        emitSource(responseLiveData)
    }

    fun getListKegiatan(): LiveData<Response<GetKegiatanResponse>> = liveData {
        val responseLiveData = MutableLiveData<Response<GetKegiatanResponse>>()
        val token = _token.value ?: ""
        val client = repository.getListKegiatan()
        client.enqueue(object : Callback<GetKegiatanResponse> {
            override fun onResponse(
                call: Call<GetKegiatanResponse>, response: Response<GetKegiatanResponse>
            ) {
                responseLiveData.value = response
            }

            override fun onFailure(call: Call<GetKegiatanResponse>, t: Throwable) {
                val errorBody = (t.message ?: "Unknown error").toResponseBody(null)
                val errorResponse = Response.error<GetKegiatanResponse>(500, errorBody)
                responseLiveData.value = errorResponse
                _errorMessage.value = t.message
            }
        })
        emitSource(responseLiveData)
    }

    fun getListKegiatanByType(type: String): LiveData<Response<GetKegiatanResponse>> = liveData {
        val responseLiveData = MutableLiveData<Response<GetKegiatanResponse>>()
        val token = _token.value ?: ""
        val client = repository.getListKegiatanByType(type)
        client.enqueue(object : Callback<GetKegiatanResponse> {
            override fun onResponse(
                call: Call<GetKegiatanResponse>, response: Response<GetKegiatanResponse>
            ) {
                responseLiveData.value = response
            }

            override fun onFailure(call: Call<GetKegiatanResponse>, t: Throwable) {
                val errorBody = (t.message ?: "Unknown error").toResponseBody(null)
                val errorResponse = Response.error<GetKegiatanResponse>(500, errorBody)
                responseLiveData.value = errorResponse
                _errorMessage.value = t.message
            }
        })
        emitSource(responseLiveData)
    }

    fun checklistKegiatan(id: Int): LiveData<Response<BasicResponse>> = liveData {
        val responseLiveData = MutableLiveData<Response<BasicResponse>>()
        val token = _token.value ?: ""
        val client = repository.checklistKegiatan(id)
        client.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>, response: Response<BasicResponse>
            ) {
                responseLiveData.value = response
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                val errorBody = (t.message ?: "Unknown error").toResponseBody(null)
                val errorResponse = Response.error<BasicResponse>(500, errorBody)
                responseLiveData.value = errorResponse
                _errorMessage.value = t.message
            }
        })
        emitSource(responseLiveData)
    }

    fun getClassDetail(class_id: String): LiveData<Response<JoinDetailClassResponse>> = liveData {
        val responseLiveData = MutableLiveData<Response<JoinDetailClassResponse>>()
        _loadingDetailClass.value = true
        val token = _token.value ?: ""
        val client = repository.getDetailClass(class_id)
        client.enqueue(object : Callback<JoinDetailClassResponse> {
            override fun onResponse(
                call: Call<JoinDetailClassResponse>, response: Response<JoinDetailClassResponse>
            ) {
                _loadingDetailClass.value = false
                responseLiveData.value = response
            }

            override fun onFailure(call: Call<JoinDetailClassResponse>, t: Throwable) {
                _loadingDetailClass.value = false
                val errorBody = (t.message ?: "Unknown error").toResponseBody(null)
                val errorResponse = Response.error<JoinDetailClassResponse>(500, errorBody)
                responseLiveData.value = errorResponse
                _errorMessage.value = t.message
            }
        })
        emitSource(responseLiveData)
    }


    fun getClassInformation(class_id: String): LiveData<Response<DetailClassResponse>> = liveData {
        val responseLiveData = MutableLiveData<Response<DetailClassResponse>>()
        _isLoading.value = true
        val token = _token.value ?: ""
        val client = repository.getClassInformation(class_id)
        client.enqueue(object : Callback<DetailClassResponse> {
            override fun onResponse(
                call: Call<DetailClassResponse>, response: Response<DetailClassResponse>
            ) {
                _isLoading.value = false
                responseLiveData.value = response
            }

            override fun onFailure(call: Call<DetailClassResponse>, t: Throwable) {
                _isLoading.value = false
                val errorBody = (t.message ?: "Unknown error").toResponseBody(null)
                val errorResponse = Response.error<DetailClassResponse>(500, errorBody)
                responseLiveData.value = errorResponse
                _errorMessage.value = t.message
            }
        })
        emitSource(responseLiveData)
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


}