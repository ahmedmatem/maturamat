package com.ahmedmatem.android.matura.network.models

import androidx.annotation.Keep

@Keep
data class ExternalLoginInfo(
    val login: UserLoginInfo,
    val defaultUsername: String,
    val email: String
)

@Keep
data class UserLoginInfo(
    val loginProvider: String,
    val providerKey: String
)
