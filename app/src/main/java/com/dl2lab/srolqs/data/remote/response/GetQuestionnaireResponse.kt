package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetQuestionnaireResponse(

	@field:SerializedName("data")
	val data: DataQuestionnaire? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class User(

	@field:SerializedName("institution")
	val institution: String? = null,

	@field:SerializedName("reset_code")
	val resetCode: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("birth_date")
	val birthDate: String? = null,

	@field:SerializedName("reset_code_expiry")
	val resetCodeExpiry: String? = null,

	@field:SerializedName("degree")
	val degree: String? = null,

	@field:SerializedName("batch")
	val batch: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("identity_number")
	val identityNumber: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class DataQuestionnaire(

	@field:SerializedName("scoreResult")
	val scoreResult: List<Float?>? = null,

	@field:SerializedName("scoreAverage")
	val scoreAverage: List<Float?>? = null,

	@field:SerializedName("user")
	val user: User? = null
)
