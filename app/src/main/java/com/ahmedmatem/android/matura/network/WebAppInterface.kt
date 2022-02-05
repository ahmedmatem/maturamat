package com.ahmedmatem.android.matura.network

import android.app.Activity
import android.content.Context
import android.webkit.JavascriptInterface
import androidx.annotation.MainThread
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.ui.test.TestViewViewModel

class WebAppInterface(
    private val context: Context,
    val viewModel: BaseViewModel? = null
) {

    @JavascriptInterface
    fun showTestResult(testId: String) {
        (viewModel as TestViewViewModel)?.let {
            it.showTestResult(testId)
        }
    }

    @JavascriptInterface
    fun postExecute(actionCode: Int) {
        when (actionCode) {
            ACTION_FINISH_ACTIVITY -> {
                (context as Activity).finish()
            }
            else -> {}
        }
    }

    companion object {
        const val NO_ACTION = 0
        const val ACTION_FINISH_ACTIVITY = 1
    }
}