package com.ahmedmatem.android.matura.infrastructure

/**
 * API configurations
 */
const val BASE_API_URL = "https://matypaapi.azurewebsites.net/"

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
