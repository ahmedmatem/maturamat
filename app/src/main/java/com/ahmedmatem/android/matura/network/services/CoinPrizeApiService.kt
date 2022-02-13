package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.prizesystem.models.CoinPrize
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface CoinPrizeApiService {
    @GET("api/prizeSystem/prize")
    suspend fun getPrize(@Query("username") username: String): CoinPrize

    @PUT("api/prizeSystem/update")
    suspend fun updatePrize(@Body prize: CoinPrize)
}

object PrizeApi {
    val coinPrizeRetrofitService: CoinPrizeApiService by lazy {
        Retrofit.instance.create(CoinPrizeApiService::class.java)
    }
}