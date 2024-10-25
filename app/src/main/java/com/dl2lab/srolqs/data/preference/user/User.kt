package com.dl2lab.srolqs.data.preference.user

import java.sql.Date

data class User (
    val id: String,
    val nama: String,
    val birthDate: String,
    val email: String,
    val password: String,
    val identityNumber: String,
    val batch: String,
    val institution: String,
    val degree: String,
    val role: String,
    val resetCode: String,
    val resetCodeExpiry: String,
    val token: String,
    val isLogin: Boolean = false
)