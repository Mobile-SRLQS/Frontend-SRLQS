package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class StudentProgressResponse(

	@field:SerializedName("data")
	val data: ProgressData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ProgressData(

	@field:SerializedName("scores")
	val scores: List<Float?>? = null,

	@field:SerializedName("description")
	val description: String? = null
)
