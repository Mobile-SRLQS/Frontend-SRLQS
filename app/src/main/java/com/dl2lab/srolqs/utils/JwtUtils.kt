package com.dl2lab.srolqs.utils

import android.util.Base64
import org.json.JSONObject
import java.util.Date

object JwtUtils {
    fun getExpiryDate(token: String): Date? {
        return try {
            val parts = token.split(".")
            if (parts.size == 3) {
                val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
                val jsonObject = JSONObject(payload)
                val exp = jsonObject.getLong("exp")
                Date(exp * 1000)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun isTokenExpired(token: String): Boolean {
        val expiryDate = getExpiryDate(token)
        return expiryDate?.before(Date()) ?: true
    }
}