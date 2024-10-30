package com.dl2lab.srolqs.data.remote.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest (
    @SerializedName("old_password") val oldPassword: String,
    @SerializedName("new_password") val newPasword: String,
    @SerializedName("confirmed_password") val confirmedPassword: String
)