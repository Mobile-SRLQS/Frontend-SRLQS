package com.dl2lab.srolqs.data.remote.request


import com.google.gson.annotations.SerializedName

data class ForgotPasswordCodeRequest (
    @SerializedName("email") val email: String,
)