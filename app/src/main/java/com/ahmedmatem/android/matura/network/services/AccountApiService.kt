package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.infrastructure.GRANT_TYPE
import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.network.models.Token
import retrofit2.http.*

interface AccountApiService {

    @FormUrlEncoded
    @POST("token")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = GRANT_TYPE
    ): Token

    @GET("api/account/emailConfirmation")
    suspend fun emailConfirmed(@Query("email") email: String): Boolean

    @FormUrlEncoded
    @POST("api/account/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") passwordConfirm: String,
        @Field("fcmRegistrationToken") fcmRegToken: String
    )
}

object AccountApi {
    val retrofitService: AccountApiService by lazy {
        Retrofit.instance.create(AccountApiService::class.java)
    }
}