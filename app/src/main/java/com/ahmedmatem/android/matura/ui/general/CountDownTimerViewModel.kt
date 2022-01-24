package com.ahmedmatem.android.matura.ui.general

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.utils.TestCountDownTimer
import com.ahmedmatem.android.matura.utils.TimerListener
import java.lang.IllegalArgumentException

class CountDownTimerViewModel(millis: Long?) : BaseViewModel(), TimerListener {

    val timer = TestCountDownTimer(millis!!, this)
    private var _pausedByUser: Boolean = false
    private var _pausedBySystem: Boolean = false

    // millisInFuture used to update UI
    private val _millisInFuture = MutableLiveData<Long?>(millis)
    val millisInFuture: LiveData<Long?> = _millisInFuture

    private val _onTimerFinish = MutableLiveData<Boolean>().apply { false }
    val onTimerFinish: LiveData<Boolean> = _onTimerFinish

    fun onTimerClick() {
        timer.pause()
    }

    fun onPause() {
        _pausedBySystem = !_pausedByUser
        timer.pause()
    }

    fun onResume() {
        if (_pausedBySystem && !_pausedByUser) {
            timer.resume()
        }
    }

    fun onBackPressed() {
        timer.pause()
    }

    /**
     * On 'Home' and 'Check' menu items press handler
     */
    fun onOptionItemSelected() {
        timer.pause()
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