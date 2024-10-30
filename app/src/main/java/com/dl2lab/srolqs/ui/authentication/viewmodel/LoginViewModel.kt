package com.dl2lab.srolqs.ui.authentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dl2lab.srolqs.data.preference.user.User
import com.dl2lab.srolqs.data.remote.request.LoginRequest
import com.dl2lab.srolqs.data.remote.response.LoginResponse
import com.dl2lab.srolqs.data.remote.retrofit.ApiConfig
import com.dl2lab.srolqs.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginUser = MutableLiveData<LoginResponse>()
    val loginUser: LiveData<LoginResponse> = _loginUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessageLogin = MutableLiveData<String>()
    val errorMessageLogin: LiveData<String> = _errorMessageLogin

    fun login(email: String, password: String) {
        _isLoading.value = true
        val request = LoginRequest(email, password)
        val client = ApiConfig.getApiService().login(request)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginUser.value = response.body()
                    response.body()?.let { loginResponse ->
                        viewModelScope.launch {
                            repository.saveSession(
                                User(
                                    id = loginResponse.loginResult.id,
                                    nama = loginResponse.loginResult.nama,
                                    birthDate = loginResponse.loginResult.birthDate,
                                    email = loginResponse.loginResult.email,
                                    password = password,
                                    identityNumber = loginResponse.loginResult.identityNumber,
                                    batch = loginResponse.loginResult.batch,
                                    institution = loginResponse.loginResult.institution,
                                    degree = loginResponse.loginResult.degree,
                                    role = loginResponse.loginResult.role,
                                    resetCode = loginResponse.loginResult.resetCode,
                                    resetCodeExpiry = loginResponse.loginResult.resetCodeExpiry,
                                    token = loginResponse.loginResult.token,
                                    isLogin = true
                                )
                            )
                        }
                    }
                } else {
                    _errorMessageLogin.value = "Login failed: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessageLogin.value = "Login failed: ${t.message}"
            }
        })
    }


    companion object {
        private const val TAG = "LoginViewModel"
    }
}