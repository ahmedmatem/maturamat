package com.ahmedmatem.android.matura.utils

import java.security.InvalidParameterException

class ManyToOneProgressTracker private constructor() {
    private var tracksProgress: MutableList<Int> = mutableListOf()

    val progress: Int
        get() {
            return tracksProgress.average().toInt()
        }

    fun updateProgress(progress: Int, trackPosition: Int) {
        if(trackPosition < 0 || tracksProgress.size - 1 < trackPosition) {
            throw IndexOutOfBoundsException("updateProgress: Track position ($trackPosition) is out of bounds.")
        }
        tracksProgress[trackPosition] = progress
    }

    companion object {
        fun from(trucksCount: Int) : ManyToOneProgressTracker {
            if(trucksCount <= 0){
                throw InvalidParameterException("ManyToOneProgressTracker.from(tracksCount): " +
                        "Trucks count ($trucksCount) must be negative.")
            }
            return ManyToOneProgressTracker().apply {
                tracksProgress = MutableList(trucksCount) { 0 }
            }
        }
    }
}