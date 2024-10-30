package com.dl2lab.srolqs.data.remote.request

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)