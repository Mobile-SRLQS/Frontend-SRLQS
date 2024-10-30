package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class LoginResult(

	@field:SerializedName("institution")
	val institution: String,

	@field:SerializedName("reset_code")
	val resetCode: Any,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("birth_date")
	val birthDate: String,

	@field:SerializedName("reset_code_expiry")
	val resetCodeExpiry: Any,

	@field:SerializedName("degree")
	val degree: String,

	@field:SerializedName("batch")
	val batch: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("identity_number")
	val identityNumber: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("token")
	val token: String
)
