package com.ahmedmatem.android.matura.network

import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import com.ahmedmatem.android.matura.TestActivity

enum class ActionCode(val code: Int) {
    NO_ACTION(0), ACTION_FINISH(1)
}

class WebAppInterface(private val context: Context) {

    @JavascriptInterface
    fun showTestResult(testId: String) {
        Intent(context, TestActivity::class.java).apply {

        }
    }

    @JavascriptInterface
    fun postExecute(actionCode: Int) {
        when (actionCode) {
            ActionCode.ACTION_FINISH.code -> {}
            else -> {}
        }
    }
}