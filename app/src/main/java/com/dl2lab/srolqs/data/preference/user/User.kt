package com.dl2lab.srolqs.data.preference.user

data class User(
    val id: Int,
    val nama: String,
    val birthDate: String,
    val email: String,
    val password: String,
    val identityNumber: String,
    val batch: String,
    val institution: String,
    val degree: String,
    val role: String,
    val resetCode: Any? = null,
    val resetCodeExpiry: Any? = null,
    val token: String,
    val isLogin: Boolean = false,
    val profilePicture: String,
)