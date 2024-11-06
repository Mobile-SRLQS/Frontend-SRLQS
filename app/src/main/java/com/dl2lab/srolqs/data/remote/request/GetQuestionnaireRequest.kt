package com.dl2lab.srolqs.data.remote.request

import com.google.gson.annotations.SerializedName

data class GetQuestionnaireRequest(
    @field:SerializedName("class_id")
    val classId: String? = null,

    @field:SerializedName("period")
    val period: String? = null,

)