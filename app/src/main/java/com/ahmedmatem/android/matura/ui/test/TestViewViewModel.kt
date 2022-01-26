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
import com.ahmedmatem.android.matura.network.WebAppInterface.Companion.ACTION_FINISH_ACTIVITY
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.ui.general.NoticeDialogFragment
import com.ahmedmatem.android.matura.ui.general.NoticeDialogTag
import com.ahmedmatem.android.matura.ui.test.contracts.TestState
import com.ahmedmatem.android.matura.utils.TestURLUtil
import com.ahmedmatem.android.matura.utils.helpers.NoticeDataCreator
import com.ahmedmatem.android.matura.utils.providers.ResourcesProvider
import com.ahmedmatem.android.matura.utils.providers.SharedPreferencesProvider
import org.koin.java.KoinJavaComponent.inject
import java.lang.IllegalArgumentException

class TestViewViewModel(var test: Test? = null) : BaseViewModel(),
    NoticeDialogFragment.NoticeDialogListener {

    private var _testChecked = false
    private var _testCanceled = false

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
//    val isListViewMode = !isCardsViewMode

    private val _onDialogPositiveClick = MutableLiveData<NoticeDialogTag?>().apply { value = null }
    val onDialogPositiveClick: LiveData<NoticeDialogTag?> = _onDialogPositiveClick

    private val _onDialogNegativeClick = MutableLiveData<NoticeDialogTag?>().apply { value = null }
    val onDialogNegativeClick: LiveData<NoticeDialogTag?> = _onDialogNegativeClick

    private val _onDialogNeutralClick = MutableLiveData<NoticeDialogTag?>().apply { value = null }
    val onDialogNeutralClick: LiveData<NoticeDialogTag?> = _onDialogNeutralClick

    // Observe this property in response of onOptionItemSelected event
    private val _onOptionItemSelected = MutableLiveData<Boolean>(false)
    val onOptionItemSelected: LiveData<Boolean> = _onOptionItemSelected

    // Observe this property in response of onOptionItemSelected event
    private val _onBackPressed = MutableLiveData<Boolean>(false)
    val onBackPressed: LiveData<Boolean> = _onBackPressed

    // Observe this property to handle timer click
    private val _onTimerClick = MutableLiveData<Boolean>()
    val onTimerCLick: LiveData<Boolean> = _onTimerClick

    private val _onActivityFinish: MutableLiveData<Boolean> = MutableLiveData(false)
    val onActivityFinish: LiveData<Boolean> = _onActivityFinish

    private val _onSaveTest = MutableLiveData<SaveTestArgs>(null)
    val onSaveTest: LiveData<SaveTestArgs> = _onSaveTest

    var millisInFuture: Long = test?.millisInFuture
        ?: _resources.getInteger(R.integer.test_duration_in_minutes) * 60 * 1000L

    init {
        // Show start notice dialog
        showNoticeDialog.value = noticeDataCreator.createStartNotice(millisInFuture)
    }

    val url: String by lazy {
        when (test?.state) {
            TestState.NOT_STARTED -> urlUtil.repeatTestUrl(test?.id!!)
            TestState.INCOMPLETE -> urlUtil.resumeTestUrl(test?.id!!)
            else -> urlUtil.newTestUrl()
        }
    }

    fun onTimerClick(millis: Long) {
        showNoticeDialog.value = noticeDataCreator.createStopNotice(millis)
        _onTimerClick.value = true
    }

    fun onBackPressed() {
        showNoticeDialog.value = noticeDataCreator.createCancelNotice()
        // If test has a timer trigger back pressed handler in timer fragment
        if (hasTimer) _onBackPressed.value = true
    }

    fun onHomePressed() {
        showNoticeDialog.value = noticeDataCreator.createCancelNotice()
        // If test has a timer trigger onOptionItemSelected handler in timer fragment
        if (hasTimer) _onOptionItemSelected.value = true
    }

    fun onCheckPressed() {
        showNoticeDialog.value = noticeDataCreator.createCheckNotice()
        // If test has a timer trigger onOptionItemSelected handler in timer fragment
        if (hasTimer) _onOptionItemSelected.value = true
    }

    fun onTimerFinish() {
        showNoticeDialog.value = noticeDataCreator.createFinishNotice()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        dialog.tag?.let { value ->
            val dialogTag = NoticeDialogTag.fromValue(value)
            when (dialogTag) {
                NoticeDialogTag.START -> {}
                NoticeDialogTag.STOP -> {}
                NoticeDialogTag.CHECK -> {}
                NoticeDialogTag.CANCEL -> {
                    _onSaveTest.value =
                        SaveTestArgs(millisInFuture, hasTimer, ACTION_FINISH_ACTIVITY)
                }
                NoticeDialogTag.FINISH -> {}
            }
            if (hasTimer) {
                _onDialogPositiveClick.value = dialogTag
            }
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        dialog.tag?.let { value ->
            val dialogTag = NoticeDialogTag.fromValue(value)
            when (dialogTag) {
                NoticeDialogTag.START -> _onActivityFinish.value = true
                NoticeDialogTag.STOP -> {}
                NoticeDialogTag.CHECK -> {}
                NoticeDialogTag.CANCEL -> _onActivityFinish.value = true
                NoticeDialogTag.FINISH -> {}
            }
            if (hasTimer) {
                _onDialogNegativeClick.value = dialogTag
            }
        }
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) {
        dialog.tag?.let { value ->
            val dialogTag = NoticeDialogTag.fromValue(value)
            when (dialogTag) {
                NoticeDialogTag.START -> {}
                NoticeDialogTag.STOP -> {}
                NoticeDialogTag.CHECK -> {}
                NoticeDialogTag.CANCEL -> {}
                NoticeDialogTag.FINISH -> {}
            }
            if (hasTimer) {
                _onDialogNeutralClick.value = dialogTag
            }
        }
    }

    data class SaveTestArgs(val millisInFuture: Long, val hasTimer: Boolean, val actionCode: Int)

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