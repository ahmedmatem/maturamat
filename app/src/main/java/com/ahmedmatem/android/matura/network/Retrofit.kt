package com.ahmedmatem.android.matura.network

import com.ahmedmatem.android.matura.infrastructure.BASE_API_URL
import com.ahmedmatem.android.matura.network.jsonadapter.DateJsonAdapter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object Retrofit {
    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
//        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .add(DateJsonAdapter())
        .build()

    private val httpClient: OkHttpClient = OkHttpClient()
        .newBuilder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
//        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_API_URL)
            .build()
    }
}
