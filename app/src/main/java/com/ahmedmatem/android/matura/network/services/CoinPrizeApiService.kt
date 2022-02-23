package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.network.models.CoinPrizeModel
import com.ahmedmatem.android.matura.prizesystem.models.CoinPrize
import retrofit2.http.*

interface CoinPrizeApiService {
    @GET("api/prizeSystem/prize")
    suspend fun getPrize(@Header("Authorization") authorization: String): CoinPrizeModel

    @PUT("api/prizeSystem/update")
    suspend fun updatePrize(
        @Header("Authorization") authorization: String,
        @Body prize: CoinPrizeModel
    )
}

object PrizeApi {
    val coinPrizeRetrofitService: CoinPrizeApiService by lazy {
        Retrofit.instance.create(CoinPrizeApiService::class.java)
    }
}