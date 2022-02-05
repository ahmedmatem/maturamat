package com.ahmedmatem.android.matura.network

import android.app.Activity
import android.content.Context
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.FrameLayout
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.ui.test.TestViewViewModel
import com.ahmedmatem.lib.mathkeyboard.MathInputEditorFragment
import com.ahmedmatem.lib.mathkeyboard.config.Constants

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

    @JavascriptInterface
    fun showKeyboard(selector: String, content: String) {
        (context as Activity).runOnUiThread(Runnable {
            val keyboardFragment = (context as FragmentActivity)
                .supportFragmentManager.findFragmentByTag(Constants.FRAGMENT_TAG) as MathInputEditorFragment
            keyboardFragment.divSelector = selector
            val displayContent = keyboardFragment.displayContent
            displayContent.setContent(content)
            val displayView = keyboardFragment.displayView
            displayView.print(displayContent)
            (context as FragmentActivity).findViewById<FrameLayout>(R.id.keyboard_container).visibility =
                View.VISIBLE
        })
    }

    companion object {
        const val NO_ACTION = 0
        const val ACTION_FINISH_ACTIVITY = 1
    }
}