package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.prizesystem.models.Prize
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface PrizeApiService {
    @GET("api/prizeSystem/prize")
    suspend fun getUserPrize(@Query("username") username: String): Prize

    @PUT("api/prizeSystem/update")
    suspend fun updatePrize(@Body prize: Prize)
}

object PrizeApi {
    val retrofitService: PrizeApiService by lazy {
        Retrofit.instance.create(PrizeApiService::class.java)
    }
}