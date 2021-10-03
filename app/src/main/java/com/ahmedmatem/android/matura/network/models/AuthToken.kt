package com.ahmedmatem.android.matura.network.models

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class AuthToken(
    @Json(name = "access_token") val token: String,
    @Json(name = "token_type") val type: String,
    @Json(name = "expires_in") val expireIn: String,
    val userName: String,
    @Json(name = ".issued") val issued: String,
    @Json(name = ".expires") val expires: String
)