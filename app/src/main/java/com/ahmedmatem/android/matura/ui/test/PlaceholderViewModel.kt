package com.ahmedmatem.android.matura.ui.test

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.ui.test.contracts.TestState
import com.ahmedmatem.android.matura.utils.TestURLUtil
import java.lang.IllegalArgumentException

class PlaceholderViewModel(private val context: Context) : BaseViewModel() {

    val urlUtil: TestURLUtil by lazy { TestURLUtil(context) }

    fun navigateByTestState(testState: Int) {
        when (testState) {
            TestState.NOT_STARTED -> startNewTest()
            TestState.INCOMPLETE -> resumeTest()
            TestState.COMPLETE -> showTestResult()
        }
    }

    private fun startNewTest() {
        val newTestUrl = urlUtil.newTestUrl()
        navigationCommand.value = NavigationCommand.To(
            PlaceholderFragmentDirections.actionPlaceholderToNewTestFragment(newTestUrl)
        )
    }

    private fun resumeTest(testId: String) {
        val resumeTestUrl = urlUtil.resumeTestUrl(testId )
    }

    private fun showTestResult() {

    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlaceholderViewModel::class.java)) {
                return PlaceholderViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct a TestActivityPlaceholderViewModel")
        }

    }
}