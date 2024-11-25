package com.dl2lab.srolqs.ui.authentication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dl2lab.srolqs.data.remote.request.RegisterRequest
import com.dl2lab.srolqs.data.remote.response.RegisterResponse
import com.dl2lab.srolqs.data.remote.retrofit.ApiConfig
import com.dl2lab.srolqs.data.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerUser = MutableLiveData<RegisterResponse>()
    val registerUser: LiveData<RegisterResponse> = _registerUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessageRegister = MutableLiveData<String>()
    val errorMessageRegister: LiveData<String> = _errorMessageRegister

    fun register(
        nama: String,
        birthDate: String,
        email: String,
        password: String,
        confirmedPassword: String,
        identityNumber: String,
        batch: String,
        institution: String,
        degree: String,
        role: String) {
        _isLoading.value = true
        val registerRequest = RegisterRequest(nama, birthDate, email, password, confirmedPassword, identityNumber, batch, institution, degree, role)
        val client = ApiConfig.getApiService().register(registerRequest)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerUser.value = response.body()
                } else {
                    _errorMessageRegister.value = "Register failed: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessageRegister.value = "Register failed: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun register(
        nama: String,
        birthDate: String,
        email: String,
        password: String,
        confirmedPassword: String,
        institution: String,
        role: String) {
        _isLoading.value = true
        val registerRequest = RegisterRequest(nama= nama, birthDate=birthDate, email=email, password = password, confirmedPassword = confirmedPassword,  institution = institution,  role = role)
        val client = ApiConfig.getApiService().register(registerRequest)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerUser.value = response.body()
                } else {
                    _errorMessageRegister.value = "Register failed: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessageRegister.value = "Register failed: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}