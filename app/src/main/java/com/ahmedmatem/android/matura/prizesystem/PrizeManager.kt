package com.ahmedmatem.android.matura.prizesystem

import android.content.Context
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.prizesystem.models.*
import com.ahmedmatem.android.matura.repository.PrizeRepository
import org.koin.java.KoinJavaComponent.inject

class PrizeManager(private val context: Context) {
    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)
//    private val prizeRepository by lazy { PrizeRepository(context) }
    private val prizeRepository: PrizeRepository by inject(PrizeRepository::class.java)

    suspend fun setupOnAppStart() {
        val prize = prizeRepository.getPrize()
        prize?.let {
            if (it.period.expired()) {
                resetPrize(it)
                prizeRepository.update(it)
            }
        }
    }

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
                    // Prize doesn't exist either on remote machine or on local machine
                    val username = _userPrefs.getUser()?.username
                    username?.let {
                        val newPrize = Prize(Coin(username), Period(username))
                        prizeRepository.syncPrize(newPrize)
                    }
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