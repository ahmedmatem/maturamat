package com.ahmedmatem.android.matura.repository

import android.content.Context
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.daos.TokenDao
import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.network.services.AuthApi

class TokenRepository(val tokenDao: TokenDao) {

    suspend fun requestToken(username: String, password: String) {
        val token = AuthApi.retrofitService.getToken(username, password)
        tokenDao.insert(token!!)
    }
}