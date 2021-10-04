package com.ahmedmatem.android.matura.infrastructure.extensions

const val NON_ALPHANUMERIC: String = "&()-[{}]:;',.?/*^%!|=+-_`#@"

/**
 * Check if a string contains digit
 */
fun String.hasDigit(): Boolean {
    this?.forEach { ch ->
        if (ch.isDigit()) return true
    }
    return false
}

/**
 * Check if a string contains lowerCase
 */
fun String.hasLowerCase(): Boolean {
    this?.forEach { ch ->
        if (ch.isLowerCase()) return true
    }
    return false
}

/**
 * Check if a string contains upperCase
 */
fun String.hasUpperCase(): Boolean {
    this?.forEach { ch ->
        if (ch.isUpperCase()) return true
    }
    return false
}

/**
 * Check if a string contains at least one char contains in nonAlphaNumerics
 */
fun String.hasNonAlphaNumeric(nonAlphaNumerics: String = NON_ALPHANUMERIC): Boolean {
    this?.forEach { ch ->
        if (nonAlphaNumerics.contains(ch)) return true
    }
    return false
}

/**
 * Check if string has specific length
 */
fun String.hasLength(length: Int): Boolean {
    this?.let {
        return this.length == length
    }
}