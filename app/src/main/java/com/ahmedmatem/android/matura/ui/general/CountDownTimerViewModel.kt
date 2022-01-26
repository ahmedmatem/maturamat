package com.ahmedmatem.android.matura.ui.general

import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.utils.TestCountDownTimer
import com.ahmedmatem.android.matura.utils.TimerListener
import java.lang.IllegalArgumentException

class CountDownTimerViewModel(millis: Long?) : BaseViewModel(), TimerListener {

    private val timer = TestCountDownTimer(millis!!, this)
    private var _pausedByUser: Boolean = false
    private var _pausedBySystem: Boolean = false

    // millisInFuture used to update UI
    private val _millisInFuture = MutableLiveData<Long?>(millis)
    val millisInFuture: LiveData<Long?> = _millisInFuture

    private val _onTimerFinish = MutableLiveData<Boolean>().apply { false }
    val onTimerFinish: LiveData<Boolean> = _onTimerFinish

    fun onPause() {
        _pausedBySystem = !_pausedByUser
        timer.pause()
    }

    fun onResume() {
        if (_pausedBySystem && !_pausedByUser) {
            timer.resume()
        }
    }

    fun onTimerClick() {
        _pausedByUser = true
        timer.pause()
    }

    fun onBackPressed() {
        _pausedByUser = true
        timer.pause()
    }

    /**
     * On 'Home' and 'Check' menu items press handler
     */
    fun onOptionItemSelected() {
        _pausedByUser = true
        timer.pause()
    }

    fun onDialogPositiveClick(tag: NoticeDialogTag) {
        when (tag) {
            NoticeDialogTag.START -> {
                _pausedByUser = false
                timer.resume()
            }
            NoticeDialogTag.STOP -> {
                _pausedByUser = false
                timer.resume()
            }
            NoticeDialogTag.CHECK -> {
                timer.cancel()
                // todo: check test
            }
            NoticeDialogTag.CANCEL -> {
                timer.cancel()
                // todo: save test
            }
            NoticeDialogTag.FINISH -> {
                timer.cancel()
                // todo: check test
            }
        }
    }

    fun onDialogNegativeClick(tag: NoticeDialogTag) {
        // Todo: not implemented yet
        when (tag) {
            NoticeDialogTag.START -> {
                timer.cancel()
            }
            NoticeDialogTag.STOP -> {}
            NoticeDialogTag.CHECK -> {}
            NoticeDialogTag.CANCEL -> timer.resume()
            NoticeDialogTag.FINISH -> {}
        }
    }

    fun onDialogNeutralClick(tag: NoticeDialogTag) {
        // Todo: not implemented yet
        when (tag) {
            NoticeDialogTag.START -> {}
            NoticeDialogTag.STOP -> {}
            NoticeDialogTag.CHECK -> {
                _pausedByUser = false
                timer.resume()
            }
            NoticeDialogTag.CANCEL -> {}
            NoticeDialogTag.FINISH -> {}
        }
    }

    override fun onTimerTick(millisInFuture: Long) {
        // update timer ui
        _millisInFuture.value = millisInFuture
    }

    override fun onTimerFinish() {
        _onTimerFinish.value = true
    }

    class Factory(private val millisInFuture: Long?) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CountDownTimerViewModel::class.java)) {
                return CountDownTimerViewModel(millisInFuture) as T
            }
            throw IllegalArgumentException("Unable to construct CountDownTimerViewModel")
        }
    }

    companion object {
        const val TAG = "CountDownTimerViewModel"
    }
}