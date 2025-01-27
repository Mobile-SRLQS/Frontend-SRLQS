package com.dl2lab.srolqs.ui.authentication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dl2lab.srolqs.data.remote.request.RegisterRequest
import com.dl2lab.srolqs.data.remote.response.RegisterResponse
import com.dl2lab.srolqs.data.remote.retrofit.ApiConfig
import com.dl2lab.srolqs.data.repository.UserRepository
import org.json.JSONObject
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

    private val _personalInfo = MutableLiveData<PersonalInfo>()
    val personalInfo: LiveData<PersonalInfo> = _personalInfo

    private val _academicInfo = MutableLiveData<AcademicInfo>()
    val academicInfo: LiveData<AcademicInfo> = _academicInfo

    fun setPersonalInfo(
        name: String,
        email: String,
        dob: String,
        password: String,
        confirmPassword: String,
        role: String
    ) {
        _personalInfo.value = PersonalInfo(name, email, dob, password, confirmPassword, role)
    }

    fun setAcademicInfo(
        university: String,
        batch: String,
        npm: String,
        degree: String
    ) {
        _academicInfo.value = AcademicInfo(university, batch, npm, degree)
    }

    fun submitRegistration() {
        val personal = personalInfo.value
        val academic = academicInfo.value

        if (personal == null) {
            _errorMessageRegister.value = "Personal information is missing!"
            return
        }

        _isLoading.value = true

        val registerRequest = if (personal.role == "Student" && academic != null) {
            RegisterRequest(
                nama = personal.name,
                birthDate = personal.dob,
                email = personal.email,
                password = personal.password,
                confirmedPassword = personal.confirmPassword,
                identityNumber = academic.npm,
                batch = academic.batch,
                institution = academic.university,
                degree = academic.degree,
                role = personal.role
            )
        } else {
            RegisterRequest(
                nama = personal.name,
                birthDate = personal.dob,
                email = personal.email,
                password = personal.password,
                confirmedPassword = personal.confirmPassword,
                institution = academic?.university ?: "",
                role = personal.role
            )
        }

        val client = ApiConfig.getApiService().register(registerRequest)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerUser.value = response.body()
                } else {
                    try {
                        // Parse error response
                        val errorMessage = response.body()?.message ?: JSONObject(response.errorBody()?.string()).getString("message")
                        _errorMessageRegister.value = errorMessage
                        Log.e(TAG, "Error: $errorMessage")
                    } catch (e: Exception) {
                        _errorMessageRegister.value = "Gagal melakukan registrasi. ${e.message}."
                        Log.e(TAG, "Error parsing response: ${e.message}")
                    }
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessageRegister.value = "Gagal melakukan registrasi.${t.message}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }

    // Helper data classes for temporary storage
    data class PersonalInfo(
        val name: String,
        val email: String,
        val dob: String,
        val password: String,
        val confirmPassword: String,
        val role: String
    )

    data class AcademicInfo(
        val university: String,
        val batch: String,
        val npm: String,
        val degree: String
    )
}
