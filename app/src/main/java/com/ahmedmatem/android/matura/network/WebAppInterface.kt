package com.ahmedmatem.android.matura.network

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.TestActivity.Companion.EXTRA_TEST_ID
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.ui.test.TestViewViewModel
import com.ahmedmatem.android.matura.ui.test2.NewTest2ViewModel
import com.ahmedmatem.lib.mathkeyboard.MathInputEditorFragment
import com.ahmedmatem.lib.mathkeyboard.config.Constants
import java.lang.ClassCastException

class WebAppInterface(
    private val context: Context,
    val viewModel: BaseViewModel? = null
) {

    /**
     * @param id - problem id
     */
    @JavascriptInterface
    fun showSolution(id: String) {

    }

    @JavascriptInterface
    fun showTestResult(testId: String) {
        (viewModel as TestViewViewModel)?.let {
            it.showTestResult(testId)
        }
    }

    /**
     * This function close TestActivity launched by TestListFragment in MainActivity
     * returning testId(used to populate test with testId in local database) as a result intent.
     */
    @JavascriptInterface
    fun postExecute(actionCode: Int) {
        when (actionCode) {
            ACTION_FINISH_ACTIVITY -> {
                val activity = (context as Activity)
                val resultIntent = Intent().apply {
                    try {
                        (viewModel as TestViewViewModel).test?.let {
                            putExtra(EXTRA_TEST_ID, it.id)
                        } ?: run {
                            val bundle = Bundle().apply {
                                putString(EXTRA_TEST_ID, null)
                            }
                            putExtra(EXTRA_TEST_ID, bundle)
                        }
                    } catch (e: ClassCastException) {
                        Log.d("DEBUG", "postExecute: Class acst exception")
                    }
                }
                /**
                 * Finish activity and return testId as result to the launcher (TestListFragment)
                 */
                activity.apply {
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
            else -> {}
        }
    }

    /**
     * This function is invoked in background so run it in UI Thread
     */
    @JavascriptInterface
    fun showKeyboard(selector: String, content: String) {
        (context as FragmentActivity).apply {
            runOnUiThread(Runnable {
                val keyboardFragment =
                    supportFragmentManager.findFragmentByTag(Constants.FRAGMENT_TAG)
                            as MathInputEditorFragment

                keyboardFragment.apply {
                    divSelector = selector
                    displayContent.let {
                        // Set content
                        it.setContent(content)
                    }.also {
                        // Print content to display
                        displayView.print(displayContent)
                    }
                    // Set keyboard visibility to VISIBLE
//                    findViewById<FrameLayout>(R.id.keyboard_container).visibility = View.VISIBLE
                    (viewModel as TestViewViewModel).showKeyboard(true)
                }
            })
        }
    }

    /**
     * Reload problem after concurrency exception occurred.
     * PLace - test2 fragment pager view.
     */
    @JavascriptInterface
    fun reloadProblem(id: String) {
        viewModel as NewTest2ViewModel
        viewModel.reloadProblemById(id)
    }

    companion object {
        const val NO_ACTION = 0
        const val ACTION_FINISH_ACTIVITY = 1
    }
}