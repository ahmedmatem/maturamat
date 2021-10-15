package com.ahmedmatem.android.matura.prizesystem

import android.content.Context
import android.util.Log
import com.ahmedmatem.android.matura.repository.PrizeRepository

class PrizeManager(
    private val context: Context,
    private val username: String
) {
    private val prizeRepository by lazy { PrizeRepository(context, username) }

    suspend fun sync() {
        val prize = prizeRepository.getPrizeForUser()
        if (prize != null) {
            /**
             * Prize has already been setup for the user
             */
            Log.d("DEBUG", "sync: Prize has already been setup for the user")
        } else {
            /**
             * No prize has been setup yet
             */
            Log.d("DEBUG", "sync: No prize has been setup yet")
        }
    }
}