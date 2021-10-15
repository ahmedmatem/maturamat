package com.ahmedmatem.android.matura.prizesystem.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.PrizeManager

class SyncPrizeWorker(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val username by lazy { UserPrefs(context).getUser() }

    override suspend fun doWork(): Result {
        Log.d("DEBUG", "doWork: start for user $username")
        username?.let {
            val prizeManager = PrizeManager(context, it)
            prizeManager.sync()
            return Result.success()
        }
        return Result.failure()
    }
}