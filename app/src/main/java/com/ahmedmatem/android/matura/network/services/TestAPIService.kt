package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.network.Retrofit

interface TestApiService {

}

object TestApi {
    val retrofitService: TestApiService by lazy {
        Retrofit.instance.create(TestApiService::class.java)
    }
}

