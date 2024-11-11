package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class ShowAvailablePeriodResponse(

	@field:SerializedName("data")
	val data: List<String?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
