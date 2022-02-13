package com.ahmedmatem.android.matura.prizesystem

import android.content.Context
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.prizesystem.models.*
import com.ahmedmatem.android.matura.repository.CoinPrizeRepository
import org.koin.java.KoinJavaComponent.inject

class PrizeManager(private val context: Context) {
    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val coinPrizeRepository: CoinPrizeRepository by inject(CoinPrizeRepository::class.java)

    /**
     * Setup Coin Prize on app starting.
     */
    suspend fun setupOnAppStart() {
        val prize = coinPrizeRepository.getPrize()
        prize?.let {
            if (it.period.expired()) {
                resetCoinPrize(it)
                coinPrizeRepository.update(it)
            }
        }
    }

    suspend fun setupOnLogin() {
        // Try to get user prize from local database
        val prize = coinPrizeRepository.getPrize()
        if (prize != null) {
            // Prize has already been setup on local machine
            if (prize.period.expired()) {
                resetCoinPrize(prize)
                coinPrizeRepository.update(prize)
            }
        } else { // No prize has been setup on local machine yet
            when (val result = coinPrizeRepository.getPrizeRemote()) {
                is Result.Success -> {
                    // Prize exists on Remote machine
                    val remotePrize = result.data
                    if (remotePrize.period.expired()) {
                        remotePrize.period.reset()
                        coinPrizeRepository.sync(remotePrize)
                    } else {
                        coinPrizeRepository.update(remotePrize, true)
                    }
                }
                else -> {
                    // Prize doesn't exist either on remote machine or on local machine
                    val username = _userPrefs.getUser()?.username
                    username?.let {
                        // Create new Coin Prize and sync it
                        val coin = Coin(
                            holder = username,
                            drawableResId = R.drawable.ic_star_outline_24
                        )
                        val coinPrize = CoinPrize(coin, Period(username))
                        coinPrizeRepository.sync(coinPrize)
                    }
                }
            }
        }
    }

    /**
     * Use this function to calculate Period new bounds and reset Gift
     */
    private fun resetCoinPrize(prize: CoinPrize) {
        prize.period.reset()
        prize.coin.reset()
    }
}