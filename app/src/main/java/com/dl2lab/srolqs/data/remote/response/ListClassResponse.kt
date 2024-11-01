package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListClassResponse(
	val data: List<DataItem?>? = null,
	val error: Boolean? = null,
	val message: String? = null
)

data class DataItem(
	@SerializedName("kode_matkul")
	val kodeMatkul: String? = null,
	@SerializedName("institusi")
	val institusi: String? = null,
	@SerializedName("instructor_id")
	val instructorId: Int? = null,
	@SerializedName("created_at")
	val createdAt: String? = null,
	@SerializedName("id")
	val id: String? = null,
	@SerializedName("class_name")
	val className: String? = null,
	@SerializedName("prodi")
	val prodi: String? = null
)

