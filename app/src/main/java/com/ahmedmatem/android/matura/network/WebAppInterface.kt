package com.ahmedmatem.android.matura.network

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import com.ahmedmatem.android.matura.TestActivity

class WebAppInterface(private val context: Context) {

    @JavascriptInterface
    fun showTestResult(testId: String) {
        Intent(context, TestActivity::class.java).apply {

        }
    }

    @JavascriptInterface
    fun postExecute(actionCode: Int) {
        when (actionCode) {
            ACTION_FINISH_ACTIVITY -> (context as Activity).finish()
            else -> {}
        }
    }

    companion object {
        const val NO_ACTION = 0
        const val ACTION_FINISH_ACTIVITY = 1
    }
}