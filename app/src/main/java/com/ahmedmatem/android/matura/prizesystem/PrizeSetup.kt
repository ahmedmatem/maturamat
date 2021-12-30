package com.ahmedmatem.android.matura.prizesystem

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.worker.PrizeSetupOnAppStartWorker
import com.ahmedmatem.android.matura.prizesystem.worker.PrizeSetupOnLoginWorker
import org.koin.java.KoinJavaComponent.inject

object PrizeSetup {
    private var username: String? = null
    private val userPrefs: UserPrefs by inject(UserPrefs::class.java)

    fun onLogin(context: Context) {
        username = userPrefs.getUser()?.username
        username?.let {
            val request = OneTimeWorkRequest.from(PrizeSetupOnLoginWorker::class.java)
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    fun onAppStart(context: Context) {
        username = userPrefs.getUser()?.username
        username?.let {
            val request = OneTimeWorkRequest.from(PrizeSetupOnAppStartWorker::class.java)
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    fun onPrizeChanged(context: Context) {
        username = userPrefs.getUser()?.username
        username?.let {
            TODO("Not implemented yet")
        }
    }
}