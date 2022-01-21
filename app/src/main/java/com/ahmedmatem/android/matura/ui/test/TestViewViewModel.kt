package com.ahmedmatem.android.matura.ui.test

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.ui.general.NoticeData
import com.ahmedmatem.android.matura.ui.general.NoticeDialogFragment
import com.ahmedmatem.android.matura.ui.test.contracts.TestState
import com.ahmedmatem.android.matura.utils.TestURLUtil
import java.lang.IllegalArgumentException

class TestViewViewModel(
    private val context: Context,
    private val args: TestViewFragmentArgs
) : BaseViewModel(), NoticeDialogFragment.NoticeDialogListener {

    private val urlUtil: TestURLUtil by lazy { TestURLUtil(context) }
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val test: Test? = args.test

    init {
        showNoticeDialog.value = NoticeData(
            "title",
            "message",
            "Ok",
            null,
            null,
            "tag"
        )
    }

    val url: String by lazy {
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

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }
}