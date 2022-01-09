package com.ahmedmatem.android.matura.ui.test

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.ui.test.contracts.TestState
import com.ahmedmatem.android.matura.utils.TestURLUtil
import java.lang.IllegalArgumentException

class TestActivityPlaceholderViewModel(private val context: Context) : BaseViewModel() {

    fun navigateByTestState(testState: Int) {
        when (testState) {
            TestState.NOT_STARTED -> startNewTest()
            TestState.INCOMPLETE -> {}
            TestState.COMPLETE -> {}
        }
    }

    private fun startNewTest() {
        val newTestUrl = TestURLUtil(context).newTestUrl()
        navigationCommand.value = NavigationCommand.To(
            TestActivityPlaceholderFragmentDirections.actionPlaceholderToNewTestFragment(newTestUrl)
        )
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TestActivityPlaceholderViewModel::class.java)) {
                return TestActivityPlaceholderViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct a TestActivityPlaceholderViewModel")
        }

    }
}