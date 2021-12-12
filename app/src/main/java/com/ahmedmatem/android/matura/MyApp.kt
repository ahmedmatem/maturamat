package com.ahmedmatem.android.matura

import android.app.Application
import android.util.Log
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.Retrofit
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.network.services.AccountApiService
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.utils.safeApiCall
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers


class MyApp : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        /**
         * Refresh user access token if it is expired.
         *
         * UserPref contains information for currently logged in user.
         */
        UserPrefs(applicationContext).getUser()?.let { user ->
            val db = MaturaDb.getInstance(applicationContext)
            applicationScope.launch {
                val token = db.accountDao.getUser(user.username)
                token?.let {
                    if (it.isExpired()) {
                        val tokenResponse = safeApiCall(Dispatchers.IO) {
                            AccountApi.retrofitService.getToken(it.userName, it.password!!)
                        }
                        when (tokenResponse) {
                            is Result.Success -> {
                                db.accountDao.insert(it)
                            }
                            else -> {
                                Log.e("AppStart", "onCreate: Unable to get access token.")
                            }
                        }
                    }
                }
            }

        }
    }
}