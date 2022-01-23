package com.ahmedmatem.android.matura.ui.test

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.ui.general.NoticeDialogFragment
import com.ahmedmatem.android.matura.ui.general.NoticeDialogTag
import com.ahmedmatem.android.matura.ui.test.contracts.TestState
import com.ahmedmatem.android.matura.utils.TestCountDownTimer
import com.ahmedmatem.android.matura.utils.TestURLUtil
import com.ahmedmatem.android.matura.utils.helpers.NoticeDataCreator
import com.ahmedmatem.android.matura.utils.providers.ResourcesProvider
import com.ahmedmatem.android.matura.utils.providers.SharedPreferencesProvider
import org.koin.java.KoinJavaComponent.inject
import java.lang.IllegalArgumentException
import java.security.InvalidAlgorithmParameterException

class TestViewViewModel(var test: Test? = null) : BaseViewModel(),
    NoticeDialogFragment.NoticeDialogListener {

    // Koin injections
    private val urlUtil: TestURLUtil by inject(TestURLUtil::class.java)
    private val resourcesProvider: ResourcesProvider by inject(ResourcesProvider::class.java)
    private val sharedPreferencesProvider: SharedPreferencesProvider by inject(
        SharedPreferencesProvider::class.java
    )
    private val noticeDataCreator: NoticeDataCreator by inject(NoticeDataCreator::class.java)

    private val _resources: Resources by lazy { resourcesProvider.getResources() }
    private val _prefs: SharedPreferences by lazy {
        sharedPreferencesProvider.getDefaultSharedPreferences()
    }

    val hasTimer =
        test?.hasTimer ?: _prefs.getBoolean(_resources.getString(R.string.timer_key), true)
    val isCardsViewMode: Boolean
        get() {
            val cardsViewValue = _resources.getString(R.string.test_view_cards)
            val testViewKey = _resources.getString(R.string.test_view_key)
            val testViewDefault = _resources.getString(R.string.test_view_default)
            val currentView = _prefs.getString(testViewKey, testViewDefault)
            return currentView == cardsViewValue
        }
    val isListViewMode = !isCardsViewMode

    private val _onTimerResume = MutableLiveData<Long?>().apply { value = null }
    val onTimerResume: LiveData<Long?> = _onTimerResume

    init {
        // Show start notice dialog
        showNoticeDialog.value = noticeDataCreator.createStartNotice(test?.millisInFuture!!)
    }

    val url: String by lazy {
        when (test?.state) {
            TestState.NOT_STARTED -> urlUtil.repeatTestUrl(test?.id!!)
            TestState.INCOMPLETE -> urlUtil.resumeTestUrl(test?.id!!)
            else -> urlUtil.newTestUrl()
        }
    }

    fun onBackPressed() {
        showNoticeDialog.value = noticeDataCreator.createCancelNotice()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        when (dialog.tag) {
            NoticeDialogTag.START.tag -> {
                if (test?.hasTimer!!) {
                    _onTimerResume.value = test?.millisInFuture
                }
            }
            NoticeDialogTag.STOP.tag -> {}
            NoticeDialogTag.CHECK.tag -> {}
            else -> throw InvalidAlgorithmParameterException("Invalid tag ${dialog.tag}")
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }

    class Factory(
        private val test: Test? = null
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TestViewViewModel::class.java)) {
                return TestViewViewModel(test) as T
            }
            throw IllegalArgumentException("Unable to construct a TestViewViewModel")
        }
    }
}