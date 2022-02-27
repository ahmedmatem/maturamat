package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import retrofit2.http.*

interface CoinApiService {
    @GET("api/prizeSystem/prize")
    suspend fun getCoin(@Header("Authorization") authorization: String): Coin

    @PUT("api/prizeSystem/update")
    suspend fun updateCoin(
        @Header("Authorization") authorization: String,
        @Body coin: Coin
    )
}

object PrizeApi {
    val coinRetrofitService: CoinApiService by lazy {
        Retrofit.instance.create(CoinApiService::class.java)
    }
}