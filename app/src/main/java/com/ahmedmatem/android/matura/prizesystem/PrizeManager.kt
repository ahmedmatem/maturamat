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
        val coins = prizeRepository.getCoinsForUser()
        Log.d("DEBUG", "refreshUserPrize: coins $coins")
    }
}