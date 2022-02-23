package com.ahmedmatem.android.matura.prizesystem

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ahmedmatem.android.matura.prizesystem.worker.PrizeSetupWorker

class PrizeManager(context: Context) {

    private val workManager by lazy { WorkManager.getInstance(context) }

    fun setup() {
        val setupRequest = OneTimeWorkRequestBuilder<PrizeSetupWorker>().build()
        workManager.enqueue(setupRequest)
    }
}