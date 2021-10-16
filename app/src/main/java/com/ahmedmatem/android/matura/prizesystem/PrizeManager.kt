package com.ahmedmatem.android.matura.prizesystem

import android.content.Context
import com.ahmedmatem.android.matura.prizesystem.models.*
import com.ahmedmatem.android.matura.repository.PrizeRepository

class PrizeManager(
    private val context: Context,
    private val username: String
) {
    private val prizeRepository by lazy { PrizeRepository(context, username) }

    suspend fun sync() {
        // get user prize from local database
        val prize = prizeRepository.getPrizeForUser()
        if (prize != null) {
            // Prize has already been setup for the user
            if (prize.period.expired()) {
                resetPrize(prize)
                syncPrize(prize)
            }
        } else {
            // No prize has been setup yet
            // todo: try to get prize for the user from network

            // todo: onSuccess - check prize period.

            // todo: if period expired proceed through step 1 .. 3

            // todo: else execute step 2

            // todo: onFailure - proceed through step 1 .. 3
        }
    }

    private suspend fun resetPrize(prize: Prize) {
        // Calculate the new period bounds and reset gift
        prize.period.reset()
        prize.coin.resetGift()
    }

    private suspend fun syncPrize(prize: Prize){
        // Update prize locally
        prizeRepository.updatePrize(prize)
        // todo: step 3. update prize in the network
    }
}