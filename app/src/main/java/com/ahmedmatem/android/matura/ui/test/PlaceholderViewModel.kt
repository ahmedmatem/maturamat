package com.ahmedmatem.android.matura.ui.test

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.ui.test.contracts.TestState
import com.ahmedmatem.android.matura.utils.TestURLUtil
import java.lang.IllegalArgumentException

class PlaceholderViewModel(private val context: Context) : BaseViewModel() {

    val urlUtil: TestURLUtil by lazy { TestURLUtil(context) }

    fun navigateByTest(test: Test) {
        when (test.state) {
            TestState.NOT_STARTED -> startNewTest()
            TestState.INCOMPLETE -> resumeTest(test)
            TestState.COMPLETE -> showTestResult(test.id)
        }
    }

    private fun startNewTest() {
        val newTestUrl = urlUtil.newTestUrl()
        navigationCommand.value = NavigationCommand.To(
            PlaceholderFragmentDirections.actionPlaceholderToNewTestFragment(newTestUrl)
        )
    }

    private fun resumeTest(test: Test) {
        val resumeTestUrl = urlUtil.resumeTestUrl(test.id)
    }

    private fun showTestResult(testId: String) {
        val testResultUrl = urlUtil.testResultUrl(testId)
        navigationCommand.value = NavigationCommand.To(
            PlaceholderFragmentDirections.actionPlaceholderToTestResultFragment(testResultUrl)
        )
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