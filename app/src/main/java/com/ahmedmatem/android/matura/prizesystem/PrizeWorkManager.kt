package com.ahmedmatem.android.matura.prizesystem

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.prizesystem.models.asKeyValueMap
import com.ahmedmatem.android.matura.prizesystem.worker.CoinSetupWorker
import com.ahmedmatem.android.matura.prizesystem.worker.CoinRemoteSyncWorker
import java.lang.IllegalArgumentException

class PrizeWorkManager(context: Context) {

    private val workManager by lazy { WorkManager.getInstance(context) }

    fun setup() {
        Log.d("DEBUG", "setup: by PrizeWorkManager")
        val setupRequest = OneTimeWorkRequestBuilder<CoinSetupWorker>().build()
        workManager.enqueue(setupRequest)
    }

    fun syncRemote(coin: Coin) {
        try {
            val inputData = workDataOf(*coin.asKeyValueMap().toTypedArray())
            val syncRemoteRequest = OneTimeWorkRequestBuilder<CoinRemoteSyncWorker>()
                .setInputData(inputData)
                .build()
            workManager.enqueue(syncRemoteRequest)
        } catch (e: IllegalArgumentException) {
            Log.d(TAG, "syncRemote: ${e.message}")
        }
    }

    companion object {
        const val TAG = "PrizeWorkManager"
    }
}