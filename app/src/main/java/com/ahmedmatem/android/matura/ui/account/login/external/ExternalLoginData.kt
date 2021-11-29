package com.ahmedmatem.android.matura.ui.account.login.external

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class ExternalLoginData(
    @Json(name = "UserName") var email: String,
    @Json(name = "LoginProvider") var loginProvider: String,
    @Json(name = "ProviderKey")val providerKey: String
)
