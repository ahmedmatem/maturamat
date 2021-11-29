package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.BuildConfig
import com.ahmedmatem.android.matura.infrastructure.GRANT_TYPE
import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.ui.account.login.external.ExternalLoginData
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
    suspend fun hasEmailConfirmed(@Query("email") email: String): Boolean

    @GET("api/account/sendActivationEmail")
    suspend fun sendEmailConfirmationLink(@Query("email") email: String)

    @FormUrlEncoded
    @POST("api/account/tokenSignIn")
    suspend fun tokenSignIn(
        @Field("IdToken") idToken: String,
        @Field("LoginProvider") loginProvider: String,
        @Field("SecretKey") secretKey: String = BuildConfig.EXTERNAL_LOGIN_SECRET_KEY
    ): ExternalLoginData

    @FormUrlEncoded
    @POST("api/account/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") passwordConfirm: String,
        @Field("fcmRegistrationToken") fcmRegToken: String
    )

    @FormUrlEncoded
    @POST("api/account/updateFCMRegistrationToken")
    suspend fun sendFcmRegistrationToServer(
        @Field("email") email: String,
        @Field("token") token: String
    )

    @FormUrlEncoded
    @POST("api/account/forgotPassword")
    suspend fun forgotPassword(@Field("email") email: String)
}

object AccountApi {
    val retrofitService: AccountApiService by lazy {
        Retrofit.instance.create(AccountApiService::class.java)
    }
}