package com.ahmedmatem.android.matura

import android.app.Application
import android.util.Log
import com.ahmedmatem.android.matura.infrastructure.di.applicationModule
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.utils.safeApiCall
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class MyApp : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        // Facebook Logging App Activation
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(applicationModule)
        }

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
                    if (it.isTokenExpired()) {
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