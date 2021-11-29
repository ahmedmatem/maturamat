package com.ahmedmatem.android.matura.prizesystem

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.worker.PrizeSetupOnAppStartWorker
import com.ahmedmatem.android.matura.prizesystem.worker.PrizeSetupOnLoginWorker

object PrizeSetup {
    private const val IS_FREE_DISTRIBUTION = BuildConfig.FLAVOR_distribution == "free"

    private var username: String? = null

    fun onLogin(context: Context) {
        if (IS_FREE_DISTRIBUTION) {
            username = UserPrefs(context).getUser()?.username
            username?.let {
                val request = OneTimeWorkRequest.from(PrizeSetupOnLoginWorker::class.java)
                WorkManager.getInstance(context).enqueue(request)
            }
        }
    }

    fun onAppStart(context: Context) {
        if (IS_FREE_DISTRIBUTION) {
            username = UserPrefs(context).getUser()?.username
            username?.let {
                val request = OneTimeWorkRequest.from(PrizeSetupOnAppStartWorker::class.java)
                WorkManager.getInstance(context).enqueue(request)
            }
        }
    }

    fun onPrizeChanged(context: Context) {
        if (IS_FREE_DISTRIBUTION) {
            username = UserPrefs(context).getUser()?.username
            username?.let {
                TODO("Not implemented yet")
            }
        }
    }
}