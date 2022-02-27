package com.ahmedmatem.android.matura.prizesystem.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.models.*
import com.ahmedmatem.android.matura.repository.CoinRepository
import org.koin.java.KoinJavaComponent.inject

/**
 * This class should be used only in APP FREE DISTRIBUTION after USER had logged in.
 */
class CoinSetupWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val coinRepo: CoinRepository by inject(CoinRepository::class.java)

    override suspend fun doWork(): Result {
        val localCoin = coinRepo.getCoin()
        localCoin?.let {
            if (it.expired()) {
                it.reset()
                coinRepo.sync(it)
            }
        } ?: run {
            val remoteCoin = coinRepo.getCoinRemote()
            remoteCoin?.let {
                coinRepo.update(remoteCoin)
            } ?: run {
                val user = userPrefs.getUser()
                user?.let {
                    val coin = Coin(it.username)
                    coinRepo.sync(coin)
                } ?: run { Result.failure() }
            }
        }

        return Result.success()
    }

    companion object {
        const val TAG = "PrizeSetupWorker"
    }
}