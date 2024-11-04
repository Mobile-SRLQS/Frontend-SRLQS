package com.dl2lab.srolqs.utils

import org.json.JSONObject
import retrofit2.Response

object ExtractErrorMessage {
    fun <T> extractErrorMessage(response: Response<T>): String {
        return try {
            val json = response.errorBody()?.string()
            val jsonObject = JSONObject(json)

            when {
                jsonObject.has("message") -> jsonObject.getString("message")
                else -> "Unknown Error"
            }
        } catch (e: Exception) {
            "Unknown Error"
        }
    }
}