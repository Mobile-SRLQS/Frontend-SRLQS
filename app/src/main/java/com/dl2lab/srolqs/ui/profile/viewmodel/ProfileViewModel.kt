package com.dl2lab.srolqs.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dl2lab.srolqs.data.preference.user.User
import com.dl2lab.srolqs.data.remote.request.ChangePasswordRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.EditProfileResponse
import com.dl2lab.srolqs.data.remote.response.GetProfileResponse
import com.dl2lab.srolqs.data.remote.retrofit.ApiConfig
import com.dl2lab.srolqs.data.repository.SecuredRepository
import com.dl2lab.srolqs.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File


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

    private val _editProfileData = MutableLiveData<Response<EditProfileResponse>>()
    val editProfileData: LiveData<Response<EditProfileResponse>> = _editProfileData

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

    fun editProfile(
        nama: RequestBody,
        birthDate: RequestBody,
        institution: RequestBody,
        degree: RequestBody,
        identityNumber: RequestBody,
        batch: RequestBody,
        profilePicture: MultipartBody.Part?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val apiService = ApiConfig.getApiServiceSecured(_token.value ?: "")

                val response = apiService.editProfile(
                    nama = nama,
                    birthDate = birthDate,
                    institution = institution,
                    degree = degree,
                    identityNumber = identityNumber,
                    batch = batch,
                    profilePicture = profilePicture
                ).execute()

                _editProfileData.postValue(response)
            } catch (e: Exception) {
                _errorMessage.postValue(e.message ?: "An unknown error occurred")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getDetailProfile(): LiveData<User?> {
        val userLiveData = MutableLiveData<User?>()
        _isLoading.value = true
        repository.getDetailProfile().enqueue(object : retrofit2.Callback<GetProfileResponse> {
            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()?.data
                    val user = data?.let {
                        User(
                            id = it.id ?: 0,
                            nama = it.nama.orEmpty(),
                            birthDate = it.birthDate.orEmpty(),
                            email = it.email.orEmpty(),
                            password = "", // No password in response
                            identityNumber = it.identityNumber.orEmpty(),
                            batch = it.batch.orEmpty(),
                            institution = it.institution.orEmpty(),
                            degree = it.degree.orEmpty(),
                            role = it.role.orEmpty(),
                            resetCode = null,
                            resetCodeExpiry = null,
                            token = _token.value.orEmpty(), // Keep token from the session
                            isLogin = true,
                            profilePicture = it.userProfile.orEmpty()
                        )
                    }
                    userLiveData.value = user
                } else {
                    userLiveData.value = null
                }
            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                _isLoading.value = false
                userLiveData.value = null
                _errorMessage.value = t.message
            }
        })
        return userLiveData
    }


}