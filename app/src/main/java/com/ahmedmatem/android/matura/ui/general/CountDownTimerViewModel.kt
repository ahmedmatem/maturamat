package com.ahmedmatem.android.matura.ui.general

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedmatem.android.matura.base.BaseViewModel
import com.ahmedmatem.android.matura.utils.TestCountDownTimer
import java.lang.IllegalArgumentException

class CountDownTimerViewModel(private val millis: Long?) : ViewModel(),
    TestCountDownTimer.TimerListener {

    val timer = TestCountDownTimer.create(millis!!, this)

    // millisInFuture
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

    fun timerResume() {
        timer.resume()
    }

    override fun onTimerTick(millisInFuture: Long) {
        _millisInFuture.value = millisInFuture
    }

    override fun onTimerFinish() {
        TODO("Not yet implemented")
    }
}