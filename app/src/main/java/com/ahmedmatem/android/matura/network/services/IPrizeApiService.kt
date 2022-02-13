package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.prizesystem.contract.IPrizeItem
import com.ahmedmatem.android.matura.prizesystem.models.Prize
import retrofit2.http.Body
import retrofit2.http.Query

interface IPrizeApiService {

    suspend fun <T: IPrizeItem> getPrize(@Query("username") username: String): Prize<T>

    suspend fun <T: IPrizeItem> updatePrize(@Body prize: Prize<T>)
}