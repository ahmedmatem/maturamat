package com.ahmedmatem.android.matura.utils

import android.os.CountDownTimer

class Timer private constructor(
    var millisInFuture: Long,
    private val listener: TimerListener
) : CountDownTimer(millisInFuture, COUNT_DOWN_INTERVAL) {

    interface TimerListener {
        fun onTimerTick(millisInFuture: Long)
        fun onTimerFinish()
    }

    override fun onTick(millisInFuture: Long) {
        this.millisInFuture = millisInFuture
        listener?.run {
            onTimerTick(millisInFuture)
        }
    }

    override fun onFinish() {
        listener?.run {
            onTimerFinish()
        }
    }

    fun pause() = cancel()

    companion object {
        private const val COUNT_DOWN_INTERVAL: Long = 1000 // 1 sec

        fun create(millisInFuture: Long, listener: TimerListener): Timer {
            return  Timer(millisInFuture, listener)
        }
    }
}