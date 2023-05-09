package com.ahmedmatem.android.matura.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val error: String,
    @Json(name = "error_description") val description: String
)

fun ErrorResponse.descriptionBg(): String? {
    description?.let {
        return errorTranslationMap[it]
    }
    return description
}

private val errorTranslationMap = mapOf<String, String>(
    Pair("The user name or password is incorrect.", "Грешно потребителско име или парола.")
)