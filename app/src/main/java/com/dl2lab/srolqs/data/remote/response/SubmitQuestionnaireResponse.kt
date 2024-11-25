package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class SubmitQuestionnaireResponse(

	@field:SerializedName("data")
	val data: QuestionnaireData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class QuestionnaireData(

	@field:SerializedName("question_22")
	val question22: Int? = null,

	@field:SerializedName("question_21")
	val question21: Int? = null,

	@field:SerializedName("question_9")
	val question9: Int? = null,

	@field:SerializedName("question_24")
	val question24: Int? = null,

	@field:SerializedName("question_23")
	val question23: Int? = null,

	@field:SerializedName("submitted_at")
	val submittedAt: String? = null,

	@field:SerializedName("ts_score")
	val tsScore: Int? = null,

	@field:SerializedName("question_20")
	val question20: Int? = null,

	@field:SerializedName("question_19")
	val question19: Int? = null,

	@field:SerializedName("hs_score")
	val hsScore: Any? = null,

	@field:SerializedName("question_18")
	val question18: Int? = null,

	@field:SerializedName("gs_score")
	val gsScore: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("period_id")
	val periodId: Int? = null,

	@field:SerializedName("question_11")
	val question11: Int? = null,

	@field:SerializedName("question_10")
	val question10: Int? = null,

	@field:SerializedName("is_done")
	val isDone: Boolean? = null,

	@field:SerializedName("question_13")
	val question13: Int? = null,

	@field:SerializedName("question_12")
	val question12: Int? = null,

	@field:SerializedName("es_score")
	val esScore: Any? = null,

	@field:SerializedName("tm_score")
	val tmScore: Int? = null,

	@field:SerializedName("question_15")
	val question15: Int? = null,

	@field:SerializedName("question_14")
	val question14: Int? = null,

	@field:SerializedName("question_17")
	val question17: Int? = null,

	@field:SerializedName("question_16")
	val question16: Int? = null,

	@field:SerializedName("student_id")
	val studentId: Int? = null,

	@field:SerializedName("question_3")
	val question3: Int? = null,

	@field:SerializedName("question_4")
	val question4: Int? = null,

	@field:SerializedName("se_score")
	val seScore: Int? = null,

	@field:SerializedName("question_1")
	val question1: Int? = null,

	@field:SerializedName("question_2")
	val question2: Int? = null,

	@field:SerializedName("question_7")
	val question7: Int? = null,

	@field:SerializedName("question_8")
	val question8: Int? = null,

	@field:SerializedName("question_5")
	val question5: Int? = null,

	@field:SerializedName("question_6")
	val question6: Int? = null
)
