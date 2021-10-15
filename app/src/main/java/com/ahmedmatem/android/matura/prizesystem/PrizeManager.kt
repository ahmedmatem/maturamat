package com.ahmedmatem.android.matura.prizesystem

import android.content.Context
import com.ahmedmatem.android.matura.prizesystem.models.expired
import com.ahmedmatem.android.matura.repository.PrizeRepository

class PrizeManager(
    private val context: Context,
    private val username: String
) {
    private val prizeRepository by lazy { PrizeRepository(context, username) }

    suspend fun sync() {
        // get user prize from database
        val prize = prizeRepository.getPrizeForUser()
        if (prize != null) {
            // Prize has already been setup for the user
            TODO("Not implemented yet")
            if (prize.period.expired()) {
                // todo: step 1. calculate the next period bounds

                // todo: step 2. update prize locally with new period and new gift

                // todo: step 3. sync new user prize in the network
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
}