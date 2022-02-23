package com.ahmedmatem.android.matura

import android.app.Application
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ahmedmatem.android.matura.infrastructure.FlavorDistribution
import com.ahmedmatem.android.matura.infrastructure.di.applicationModule
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.services.AccountApi
import com.ahmedmatem.android.matura.prizesystem.PrizeManager
import com.ahmedmatem.android.matura.ui.test.worker.TestListRefreshWorker
import com.ahmedmatem.android.matura.utils.safeApiCall
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {

    private val _userPrefs: UserPrefs by inject()
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val workManager by lazy { WorkManager.getInstance(applicationContext) }

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(applicationModule)
        }

        // Refresh Test List in local db
        val testListRefreshRequest = OneTimeWorkRequestBuilder<TestListRefreshWorker>().build()
        workManager.enqueue(testListRefreshRequest)

        /**
         * PRIZE SETUP - onAppStart
         */
        if (BuildConfig.FLAVOR_distribution == FlavorDistribution.FREE) {
            _userPrefs.getUser()?.let {
                PrizeManager(applicationContext).setup()
            }
        }

        // Facebook Logging App Activation
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        // Todo: (5/9) Implement token refresh as background service
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