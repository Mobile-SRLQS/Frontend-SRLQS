package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class EditProfileResponse(

	@field:SerializedName("data")
	val data: ProfileData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ProfileData(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("birth_date")
	val birthDate: String? = null,

	@field:SerializedName("degree")
	val degree: String? = null,

	@field:SerializedName("batch")
	val batch: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: Any? = null,

	@field:SerializedName("identity_number")
	val identityNumber: String? = null,

	@field:SerializedName("institution")
	val institution: String? = null,

	@field:SerializedName("reset_code")
	val resetCode: Any? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("reset_code_expiry")
	val resetCodeExpiry: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
