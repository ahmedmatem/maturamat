package com.ahmedmatem.android.matura.prizesystem.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahmedmatem.android.matura.prizesystem.models.asCoin
import com.ahmedmatem.android.matura.repository.CoinRepository
import org.koin.java.KoinJavaComponent.inject

class CoinRemoteSyncWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val coinRepo: CoinRepository by inject(CoinRepository::class.java)

    override suspend fun doWork(): Result {
        val inputDataAsKeyValueMap = inputData.keyValueMap
        val coin = inputDataAsKeyValueMap.asCoin()
        val syncResult = coinRepo.syncRemote(coin)
        return if (syncResult) {
            Result.success()
        } else {
            Result.failure()
        }
    }
}