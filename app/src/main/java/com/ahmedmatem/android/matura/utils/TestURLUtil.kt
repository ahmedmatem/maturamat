package com.ahmedmatem.android.matura.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import org.koin.java.KoinJavaComponent.inject

class TestURLUtil(ctx: Context) {
    private val _context: Context = ctx
    private val userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx)

    fun newTestUrl(): String {
        // Get url parameters keys
        val testViewKey = _context.getString(R.string.test_view_key)
        val testSizeKey = _context.getString(R.string.test_size_key)
        // Get url parameters default values
        val testViewDefault = _context.getString(R.string.test_view_default)
        val testSizeDefault = _context.resources.getInteger(R.integer.default_test_size)
        // Get url parameters values defined in app settings fragment and saved as shared preferences
        val testView = sharedPrefs.getString(testViewKey, testViewDefault)
        val testSize = sharedPrefs.getInt(testSizeKey, testSizeDefault)

        // Build new test Url
        var newTestUrl: StringBuilder = StringBuilder(_context.getString(R.string.test_new_url))
        // Append testSize parameter if different from default value
        if (testSize != testSizeDefault) {
            newTestUrl.append("/$testSize")
        }
        // Append user credentials
        val user = userPrefs.getUser()
        if (user != null) {
            newTestUrl.append("?email=${user.username}&password=${user.password}")
        } else {
            newTestUrl.append("?uuid=${userPrefs.getUuid()}")
        }

        // Append testView
        if (testView == _context.getString(R.string.test_view_cards)) {
            newTestUrl.append("&viewMode=cards")
        }

        return newTestUrl.toString()
    }

    fun resumeTestUrl(testId: String): String {
        var resumeTestUrl: StringBuilder =
            StringBuilder(_context.getString(R.string.test_resume_url))
        resumeTestUrl.append("?testId=$testId")

        val testViewKey = _context.getString(R.string.test_view_key)
        val testViewDefault = _context.getString(R.string.test_view_default)
        val testView = sharedPrefs.getString(testViewKey, testViewDefault)
        if (testView == _context.getString(R.string.test_view_cards)) {
            resumeTestUrl.append("&viewMode=cards")
        }

        return resumeTestUrl.toString()
    }

    fun repeatTestUrl(testId: String): String {
        var repeatTestUrl: StringBuilder =
            StringBuilder(_context.getString(R.string.test_repeat_url))
        repeatTestUrl.append("?testId=$testId")

        val testViewKey = _context.getString(R.string.test_view_key)
        val testViewDefault = _context.getString(R.string.test_view_default)
        val testView = sharedPrefs.getString(testViewKey, testViewDefault)
        if (testView == _context.getString(R.string.test_view_cards)) {
            repeatTestUrl.append("&viewMode=cards")
        }

        return repeatTestUrl.toString()
    }
}