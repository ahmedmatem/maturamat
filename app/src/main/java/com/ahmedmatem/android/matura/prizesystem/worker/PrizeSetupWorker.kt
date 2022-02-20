package com.ahmedmatem.android.matura.prizesystem.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.models.*
import com.ahmedmatem.android.matura.repository.CoinPrizeRepository
import org.koin.java.KoinJavaComponent.inject

/**
 * This class should be used only in APP FREE DISTRIBUTION after USER had logged in.
 */
class PrizeSetupWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val prizeRepo: CoinPrizeRepository by inject(CoinPrizeRepository::class.java)

    override suspend fun doWork(): Result {
        val localPrize = prizeRepo.getPrize()
        localPrize?.let {
            if (it.period.expired()) {
                it.reset()
                prizeRepo.sync(it)
            }
        } ?: run {
            val remotePrize = prizeRepo.getPrizeRemote()
            remotePrize?.let {
                prizeRepo.update(remotePrize, true)
            } ?: run {
                val user = userPrefs.getUser()
                user?.let {
                    val coin = Coin(
                        holder = it.username,
                        drawableResId = R.drawable.ic_star_outline_24
                    )
                    val period = Period(it.username)
                    val coinPrize = CoinPrize(coin, period)
                    prizeRepo.sync(coinPrize)
                } ?: run { Result.failure() }
            }
        }

        return Result.success()
    }
}