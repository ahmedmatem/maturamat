package com.ahmedmatem.android.matura.prizesystem

import android.content.Context
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.prizesystem.models.*
import com.ahmedmatem.android.matura.repository.PrizeRepository

class PrizeManager(
    private val context: Context,
    private val username: String
) {
    private val prizeRepository by lazy { PrizeRepository(context, username) }

    suspend fun setupOnLogin() {
        // Try to get user prize from local database
        val prize = prizeRepository.getPrize()
        if (prize != null) {
            // Prize has already been setup on local machine
            if (prize.period.expired()) {
                resetPrize(prize)
                prizeRepository.update(prize)
            }
        } else { // No prize has been setup on local machine yet
            when (val result = prizeRepository.getPrizeFromNetwork()) {
                is Result.Success -> {
                    // Prize exists on Remote machine
                    val remotePrize = result.data
                    if (remotePrize.period.expired()) {
                        remotePrize.period.reset()
                        prizeRepository.syncPrize(remotePrize)
                    } else {
                        prizeRepository.update(remotePrize, true)
                    }
                }
                else -> {
                    // Prize doesn't exist either on remote machine
                    val newPrize = Prize(Coin(username), Period(username))
                    prizeRepository.syncPrize(newPrize)
                }
            }
        }
    }

    /**
     * Use this function to calculate Period new bounds and reset Gift
     */
    private suspend fun resetPrize(prize: Prize) {
        prize.period.reset()
        prize.coin.resetGift()
    }
}