package com.ahmedmatem.android.matura.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val error: String,
    @Json(name = "error_description") val description: String
)