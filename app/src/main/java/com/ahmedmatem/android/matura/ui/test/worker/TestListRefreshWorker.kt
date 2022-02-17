package com.ahmedmatem.android.matura.ui.test.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahmedmatem.android.matura.repository.TestRepository
import org.koin.java.KoinJavaComponent.inject

class TestListRefreshWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    private val testRepo: TestRepository by inject(TestRepository::class.java)

    override suspend fun doWork(): Result {
        testRepo.refreshTestList()
        return Result.success()
    }

}