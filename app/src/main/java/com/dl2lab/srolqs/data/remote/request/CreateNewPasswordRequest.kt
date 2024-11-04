package com.dl2lab.srolqs.data.remote.request

import com.google.gson.annotations.SerializedName

data class CreateNewPasswordRequest(
    @SerializedName("email") val email: String,
    @SerializedName("new_password") val newPassword: String,
    @SerializedName("reset_code") val resetCode: String
)