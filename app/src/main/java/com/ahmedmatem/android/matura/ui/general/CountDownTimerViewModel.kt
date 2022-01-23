package com.ahmedmatem.android.matura.ui.general

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.utils.TestCountDownTimer
import com.ahmedmatem.android.matura.utils.TimerListener
import java.lang.IllegalArgumentException

class CountDownTimerViewModel(private val millis: Long?) : ViewModel(), TimerListener {

    val timer = TestCountDownTimer(millis!!, this)
    private var _pausedByUser: Boolean = false
    private var _pausedBySystem: Boolean = false


    // millisInFuture used to update UI
    private val _millisInFuture = MutableLiveData<Long?>(millis)
    val millisInFuture: LiveData<Long?> = _millisInFuture

    class Factory(private val millisInFuture: Long?) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CountDownTimerViewModel::class.java)) {
                return CountDownTimerViewModel(millisInFuture) as T
            }
            throw IllegalArgumentException("Unable to construct CountDownTimerViewModel")
        }

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

    override fun onTimerTick(millisInFuture: Long) {
        // update timer ui
        _millisInFuture.value = millisInFuture
    }

    override fun onTimerFinish() {
        Log.d(TAG, "onTimerFinish: finish")
    }

    companion object {
        const val TAG = "CountDownTimerViewModel"
    }
}