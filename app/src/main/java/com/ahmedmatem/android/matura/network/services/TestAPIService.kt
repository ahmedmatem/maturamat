package com.ahmedmatem.android.matura.network.services

import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.network.models.Test
import retrofit2.http.*

interface TestApiService {
    @GET("api/test/allByGuest/{uuid}")
    suspend fun getAllTestByGuest(@Path("uuid") uuid: String): List<Test>

    @GET("api/test/allByUser")
    suspend fun getAllTestByUser(@Header("Authorization") authorization: String): List<Test>

    @DELETE("api/test/deleteResult")
    suspend fun deleteTestResult(@Query("id") id: String)

    @PUT("api/test/restoreResult")
    suspend fun restoreTestResult(@Query("id") id: String)
}

object TestApi {
    val retrofitService: TestApiService by lazy {
        Retrofit.instance.create(TestApiService::class.java)
    }
}

