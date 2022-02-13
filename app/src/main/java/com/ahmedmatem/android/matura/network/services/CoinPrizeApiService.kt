package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.prizesystem.contract.IPrizeItem
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.prizesystem.models.Prize
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface CoinPrizeApiService: IPrizeApiService {
    @GET("api/prizeSystem/prize")
    override suspend fun <T: IPrizeItem> getPrize(@Query("username") username: String): Prize<T>

    @PUT("api/prizeSystem/update")
    override suspend fun <T: IPrizeItem> updatePrize(@Body prize: Prize<T>)
}

object PrizeApi {
    val retrofitService: IPrizeApiService by lazy {
        Retrofit.instance.create(IPrizeApiService::class.java)
    }
}