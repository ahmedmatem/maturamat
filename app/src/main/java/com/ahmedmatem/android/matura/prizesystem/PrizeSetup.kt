package com.ahmedmatem.android.matura.prizesystem

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.worker.PrizeSetupOnAppStartWorker
import com.ahmedmatem.android.matura.prizesystem.worker.PrizeSetupOnLoginWorker
import org.koin.java.KoinJavaComponent.inject

object PrizeSetup {
    private val userPrefs: UserPrefs by inject(UserPrefs::class.java)

    fun onLogin(context: Context) {
        val user = userPrefs.getUser()
        user?.let {
            val request = OneTimeWorkRequest.from(PrizeSetupOnLoginWorker::class.java)
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    fun onAppStart(context: Context) {
        val user = userPrefs.getUser()
        user?.let {
            val request = OneTimeWorkRequest.from(PrizeSetupOnAppStartWorker::class.java)
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    fun onPrizeChanged(context: Context) {
        val user = userPrefs.getUser()
        user?.let {
            TODO("Not implemented yet")
        }
    }
}