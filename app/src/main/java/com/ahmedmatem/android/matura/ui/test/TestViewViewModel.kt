package com.ahmedmatem.android.matura.ui.test

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.ui.test.contracts.TestState
import com.ahmedmatem.android.matura.utils.TestURLUtil
import java.lang.IllegalArgumentException

class TestViewViewModel(
    private val context: Context,
    private val args: TestViewFragmentArgs
) : BaseViewModel() {

    private val urlUtil: TestURLUtil by lazy { TestURLUtil(context) }

    val url: String by lazy {
        val test = args.test
        when (test?.state) {
            // Test is created but not started yet
            TestState.NOT_STARTED -> urlUtil.repeatTestUrl(test?.id)
            // Test is created and started but not finished
            TestState.INCOMPLETE -> urlUtil.resumeTestUrl(test?.id)
            // Test is not created yet
            else -> urlUtil.newTestUrl()
        }
    }

    class Factory(
        private val ctx: Context,
        private val args: TestViewFragmentArgs
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TestViewViewModel::class.java)) {
                return TestViewViewModel(ctx, args) as T
            }
            throw IllegalArgumentException("Unable to construct a TestViewViewModel")
        }
    }
}