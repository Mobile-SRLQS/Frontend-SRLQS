package com.dl2lab.srolqs.data.remote.request

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class RegisterRequest (
    @SerializedName("nama") val nama: String,
    @SerializedName("birth_date") val birthDate: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("confirmed_password") val confirmedPassword: String,
    @SerializedName("identity_number") val identityNumber: String? = null,
    @SerializedName("batch") val batch: String? = null,
    @SerializedName("institution") val institution: String,
    @SerializedName("degree") val degree: String? = null,
    @SerializedName("role") val role: String
) {
    constructor(
        nama: String,
        birthDate: String,
        email: String,
        password: String,
        confirmedPassword: String,
        institution: String,
        role: String
    ) : this(
        nama,
        birthDate,
        email,
        password,
        confirmedPassword,
        null,
        null,
        institution,
        null,
        role
    )
}