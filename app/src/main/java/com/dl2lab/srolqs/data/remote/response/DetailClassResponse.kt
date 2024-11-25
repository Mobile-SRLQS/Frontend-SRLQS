package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailClassResponse(

	@field:SerializedName("data")
	val data: ClassData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ClassData(

	@field:SerializedName("kode_matkul")
	val kodeMatkul: String? = null,

	@field:SerializedName("class_semester")
	val classSemester: String? = null,

	@field:SerializedName("institusi")
	val institusi: String? = null,

	@field:SerializedName("instructor_id")
	val instructorId: Int? = null,

	@field:SerializedName("class_description")
	val classDescription: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("class_name")
	val className: String? = null,

	@field:SerializedName("prodi")
	val prodi: String? = null,

	@field:SerializedName("period_data")
	val periodData: List<PeriodDataItem?>? = null,

)


data class PeriodDataItem(

	@field:SerializedName("is_done")
	val isDone: Boolean? = null,

	@field:SerializedName("class_id")
	val classId: String? = null,

	@field:SerializedName("subtitle")
	val subtitle: String? = null,

	@field:SerializedName("period_name")
	val periodName: Int? = null,

	@field:SerializedName("period_id")
	val periodId: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("is_available")
	val isAvailable: Boolean? = null

)
