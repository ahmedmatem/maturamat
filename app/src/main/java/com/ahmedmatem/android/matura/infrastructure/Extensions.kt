package com.ahmedmatem.android.matura.infrastructure

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Log
import android.view.View
import java.text.SimpleDateFormat
import java.util.*

/**
 * String EXTENSIONS
 */

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

/**
 * View EXTENSIONS
 */

//animate changing the view visibility
fun View.fadeIn() {
    this.visibility = View.VISIBLE
    this.alpha = 0f
    this.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeIn.alpha = 1f
        }
    })
}

//animate changing the view visibility
fun View.fadeOut() {
    this.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeOut.alpha = 1f
            this@fadeOut.visibility = View.GONE
        }
    })
}

/**
 * Date EXTENSIONS
 */

fun Date.toDisplayFormat(format: String): String {
    try {
        val sdf = SimpleDateFormat(format)
        return sdf.format(this)
    } catch (ex: IllegalFormatException) {
        Log.e("ERROR", "toUiFormat: Illegal format - $format", )
    }
    return "--"
}