package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetQuestionnaireResponse(

    @field:SerializedName("data") val data: DataQuestionnaire? = null,

    @field:SerializedName("error") val error: Boolean? = null,

    @field:SerializedName("message") val message: String? = null
)

data class User(

    @field:SerializedName("institution") val institution: String? = null,

    @field:SerializedName("reset_code") val resetCode: String? = null,

    @field:SerializedName("password") val password: String? = null,

    @field:SerializedName("role") val role: String? = null,

    @field:SerializedName("nama") val nama: String? = null,

    @field:SerializedName("birth_date") val birthDate: String? = null,

    @field:SerializedName("reset_code_expiry") val resetCodeExpiry: String? = null,

    @field:SerializedName("degree") val degree: String? = null,

    @field:SerializedName("batch") val batch: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("identity_number") val identityNumber: String? = null,

    @field:SerializedName("email") val email: String? = null
)

data class DataQuestionnaire(

    @field:SerializedName("scoreResult") val scoreResult: List<Float?>? = null,

    @field:SerializedName("scoreAverage") val scoreAverage: List<Float?>? = null,

    @field:SerializedName("user") val user: User? = null,
    @field:SerializedName("dimensionReccomendation") val dimensionReccomendation: DimensionReccomendation? = null
)

data class DimensionReccomendation(

    @field:SerializedName("tm_title") val tmTitle: String? = null,

    @field:SerializedName("gs_title") val gsTitle: String? = null,

    @field:SerializedName("hs_title") val hsTitle: String? = null,

    @field:SerializedName("es_title") val esTitle: String? = null,

    @field:SerializedName("se_title") val seTitle: String? = null,

    @field:SerializedName("gs_reccomendation") val gsReccomendation: String? = null,

    @field:SerializedName("tm_reccomendation") val tmReccomendation: String? = null,

    @field:SerializedName("se_reccomendation") val seReccomendation: String? = null,

    @field:SerializedName("ts_title") val tsTitle: String? = null,

    @field:SerializedName("ts_reccomendation") val tsReccomendation: String? = null,

    @field:SerializedName("es_reccomendation") val esReccomendation: String? = null,

    @field:SerializedName("hs_reccomendation") val hsReccomendation: String? = null
)
