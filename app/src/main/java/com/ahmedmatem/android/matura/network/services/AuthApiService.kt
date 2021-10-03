package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.infrastructure.GRANT_TYPE
import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.network.models.AuthToken
import retrofit2.http.Field
import retrofit2.http.GET

interface AuthApiService {
    @GET("token")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = GRANT_TYPE
    ): AuthToken?
}

object AuthApi {
    val retrofitService: AuthApiService by lazy {
        Retrofit.instance.create(AuthApiService::class.java)
    }
}