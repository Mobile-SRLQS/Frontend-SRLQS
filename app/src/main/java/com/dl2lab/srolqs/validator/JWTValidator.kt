package com.dl2lab.srolqs.validator

import android.util.Base64
import org.json.JSONObject

object JWTValidator {
    fun isTokenValid(token: String?): Boolean {
        if (token.isNullOrEmpty()) return false

        return try {
            val parts = token.split(".")
            if (parts.size != 3) return false

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val jsonObject = JSONObject(payload)
            val exp = jsonObject.getLong("exp")
            val currentTime = System.currentTimeMillis() / 1000

            currentTime < exp
        } catch (e: Exception) {
            false
        }
    }
}