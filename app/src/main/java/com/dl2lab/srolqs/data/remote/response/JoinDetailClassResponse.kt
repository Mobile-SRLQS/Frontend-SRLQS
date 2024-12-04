package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class JoinDetailClassResponse(

	@field:SerializedName("data")
	val data: ClassDetailData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ClassDetailData(

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

	@field:SerializedName("periods")
	val periods: List<PeriodsItem?>? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("class_name")
	val className: String? = null,

	@field:SerializedName("prodi")
	val prodi: String? = null
)

data class PeriodsItem(

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("class_id")
	val classId: String? = null,

	@field:SerializedName("period_name")
	val periodName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null
)
