package com.ahmedmatem.android.matura.prizesystem.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.PrizeManager

class PrizeSetupOnAppStartWorker(val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val username by lazy { UserPrefs(context).getUser()?.username }

    override suspend fun doWork(): Result {
        username?.let {
            val prizeManager = PrizeManager(context, it)
            prizeManager.setupOnAppStart()
            return Result.success()
        }
        return Result.failure()
    }
}