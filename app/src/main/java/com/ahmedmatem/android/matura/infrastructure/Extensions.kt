package com.ahmedmatem.android.matura.infrastructure

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
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
 * Check if string has length equal or greater than given len param
 */
fun String.hasLengthEqualOrGreaterThan(len: Int): Boolean {
    this?.let {
        return length >= len
    }
}

/**
 * View EXTENSIONS
 */

//animate changing the view visibility
fun View.fadeIn() {
    this.visibility = View.GONE
    this.alpha = 0f
    this.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeIn.alpha = 1f
            this@fadeIn.visibility = View.VISIBLE
        }
    })
}

//animate changing the view visibility
fun View.fadeOut() {
    this.visibility = View.VISIBLE
    this.alpha = 1f
    this.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeOut.alpha = 0f
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
        Log.e("ERROR", "toUiFormat: Illegal format - $format")
    }
    return "--"
}

fun Date.add(days: Int): Date {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, days)
    return cal.time
}

/**
 * EditText Extensions
 */

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            TODO("Not yet implemented")
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            TODO("Not yet implemented")
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}