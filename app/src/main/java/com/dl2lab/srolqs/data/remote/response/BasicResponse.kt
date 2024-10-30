package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class BasicResponse(

    @field:SerializedName("error") val error: Boolean? = null,

    @field:SerializedName("message") val message: String? = null,
    @field:SerializedName("data") val data: Data? = null

)

data class Data(
    val any: Any? = null
)
