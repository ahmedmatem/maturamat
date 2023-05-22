package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.network.models.Test2
import retrofit2.http.GET

interface Test2ApiService {
    @GET(value = "api/test2")
    suspend fun createTest2() : Test2
}

object Test2Api {
    val retrofitService: Test2ApiService by lazy {
        Retrofit.instance.create(Test2ApiService::class.java)
    }
}