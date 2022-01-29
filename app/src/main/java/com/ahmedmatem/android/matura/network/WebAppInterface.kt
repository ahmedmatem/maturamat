package com.ahmedmatem.android.matura.network

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import com.ahmedmatem.android.matura.TestActivity
import com.ahmedmatem.android.matura.repository.TestRepository
import org.koin.java.KoinJavaComponent.inject

class WebAppInterface(private val context: Context) {
    private val _testRepo: TestRepository by inject(TestRepository::class.java)

    @JavascriptInterface
    fun showTestResult(testId: String) {
        Intent(context, TestActivity::class.java).apply {

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