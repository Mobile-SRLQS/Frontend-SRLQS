package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetKegiatanDetailResponse(

	@field:SerializedName("data")
	val data: List<KegiatanItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)


