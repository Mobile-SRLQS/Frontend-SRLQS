package com.dl2lab.srolqs.data.remote.request

import com.google.gson.annotations.SerializedName

data class JoinClassRequest(
    @SerializedName("class_id") val classId: String,
)