package com.ahmedmatem.android.matura.prizesystem.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.PrizeManager

class PrizeSetupOnLoginWorker(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val username by lazy { UserPrefs(context).getUser() }

    override suspend fun doWork(): Result {
        username?.let {
            val prizeManager = PrizeManager(context, it)
            prizeManager.setupOnLogin()
            return Result.success()
        }
        return Result.failure()
    }
}