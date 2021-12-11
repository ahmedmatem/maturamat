package com.ahmedmatem.android.matura.ui.account.login.external

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class ExternalLoginData(
    @Json(name = "IdToken") var idToken: String,
    @Json(name = "LoginProvider") var loginProvider: String,
    @Json(name = "Email") var email: String?,
    @Json(name = "LoginAccompanyingAction") var accompanyingAction: Int
)
