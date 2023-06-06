package com.ahmedmatem.android.matura.infrastructure

import com.ahmedmatem.android.matura.BuildConfig

/**
 * WEB_URL
 */
const val BASE_WEB_URL = "https://matypa.azurewebsites.net/"
const val MOBILE_WEB_URL = "$BASE_WEB_URL/m/"

/**
 * API configurations
 */
const val BASE_API_URL = BuildConfig.BASE_API_URL

/**
 * Database configurations
 */
const val DB_NAME = "matura_db"

/**
 * Network configurations
 */

const val GRANT_TYPE = "password"

object PasswordOptions {
    const val REQUIRE_DIGIT = false
    const val REQUIRE_LOWERCASE = true
    const val REQUIRE_UPPERCASE = false
    const val REQUIRE_NON_ALPHANUMERIC = false
    const val REQUIRED_LENGTH: Int = 6
}
