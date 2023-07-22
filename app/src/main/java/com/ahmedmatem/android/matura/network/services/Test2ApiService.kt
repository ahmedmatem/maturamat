package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.network.models.Test2
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface Test2ApiService {
    @GET("api/test2")
    suspend fun createTest2() : Test2

    @GET("api/test2/mock")
    suspend fun getMockTest() : Test2
}

object Test2Api {
    val retrofitService: Test2ApiService by lazy {
        Retrofit.instance.create(Test2ApiService::class.java)
    }
}