package com.ahmedmatem.android.matura.utils

import android.os.CountDownTimer

interface TimerListener {
    fun onTimerTick(millisInFuture: Long)
    fun onTimerFinish()
}

class TestCountDownTimer(millis: Long, private val listener: TimerListener) {
    private var _timer: CountDownTimer? = null
    private var _millisLeft: Long = millis

    fun start() = resume()

    fun pause() {
        _timer?.cancel()
    }

    fun resume() {
        _timer = setupTimer(_millisLeft)
        _timer?.start()
    }

    private fun setupTimer(millisInFuture: Long): CountDownTimer {
        return object : CountDownTimer(millisInFuture, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                _millisLeft = millisUntilFinished
                listener?.apply {
                    onTimerTick(millisUntilFinished)
                }
            }

            override fun onFinish() {
                listener?.apply {
                    onTimerFinish()
                }
            }
        }
    }

    companion object {
        const val TAG = "TestCountDownTimer"
        const val COUNT_DOWN_INTERVAL: Long = 1000 // 1 sec
    }
}

data class TimeConverter private constructor(
    val hours: Long,
    val minutes: Long,
    val seconds: Long
) {

    fun toMillis(minutes: Long) = minutes * 60 * 1_000

    fun toTimerString(): String {
        return if (hours == 0L) {
            String.format("%02d:%02d", minutes, seconds)
        } else {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        }
    }

    fun toMessageString(): String {
        return if (hours == 0L) {
            if (seconds == 0L) {
                String.format("%d мин.", minutes)
            } else {
                String.format("%d мин. и %d сек.", minutes, seconds)
            }
        } else {
            if (minutes == 0L) {
                String.format("%d ч.", hours)
            } else {
                String.format("%d ч. и %d мин.", hours, minutes)
            }
        }
    }

    fun displayMinutes() = String.format("%d мин.", hours * 60 + minutes)

    companion object {
        @JvmStatic
        fun from(millis: Long): TimeConverter {
            if (millis < 0 || 24 * 60 * 60 * 1000 < millis) {
                throw IllegalArgumentException("Millis($millis) must be less than or equal to 24 hours")
            }
            val hours = millis / (1000 * 60 * 60)
            var millisLeft = millis % (1000 * 60 * 60)
            val minutes = millisLeft / (1000 * 60)
            millisLeft %= (1000 * 60)
            val seconds = millisLeft / 1000
            return TimeConverter(hours, minutes, seconds)
        }

        @JvmStatic
        fun from(hours: Long, minutes: Long, seconds: Long): TimeConverter {
            if (
                (hours < 0 || 23 < hours) ||
                (minutes < 0 || 59 < minutes) ||
                (seconds < 0 || 59 < seconds)
            ) {
                throw IllegalArgumentException("Invalid hours($hours) or minutes($minutes) or seconds($seconds)")
            }
            return TimeConverter(hours, minutes, seconds)
        }
    }
}