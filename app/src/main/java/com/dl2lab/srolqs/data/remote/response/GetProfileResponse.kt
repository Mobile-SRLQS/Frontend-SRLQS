package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetProfileResponse(

	@field:SerializedName("data")
	val data: DetailProfileData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DetailProfileData(

	@field:SerializedName("institution")
	val institution: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("birth_date")
	val birthDate: String? = null,

	@field:SerializedName("degree")
	val degree: String? = null,

	@field:SerializedName("batch")
	val batch: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("identity_number")
	val identityNumber: String? = null,

	@field:SerializedName("user_profile")
	val userProfile: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
