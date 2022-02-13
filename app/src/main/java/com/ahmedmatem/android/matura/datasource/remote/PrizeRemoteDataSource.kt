package com.ahmedmatem.android.matura.datasource.remote

import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.network.services.PrizeApi
import com.ahmedmatem.android.matura.network.services.IPrizeApiService
import com.ahmedmatem.android.matura.prizesystem.contract.IPrizeItem
import com.ahmedmatem.android.matura.prizesystem.models.Prize
import com.ahmedmatem.android.matura.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent

class PrizeRemoteDataSource<T : IPrizeItem>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val prizeApiService: IPrizeApiService by lazy { PrizeApi.retrofitService }

    private val _userPrefs: UserPrefs by KoinJavaComponent.inject(UserPrefs::class.java)
    private val username by lazy { _userPrefs.getUser()?.username }
    private val uuid by lazy { _userPrefs.getUuid() }
    private val usernameOrUuid by lazy { username ?: uuid }

    suspend fun getPrize(): Result<Prize<T>> {
        return safeApiCall(dispatcher) {
            prizeApiService.getPrize(usernameOrUuid)
        }
    }

    suspend fun sync(prize: Prize<T>): Boolean = update(prize) is Result.Success

    private suspend fun update(prize: Prize<T>): Result<Unit> {
        return safeApiCall(dispatcher) {
            prizeApiService.updatePrize(prize)
        }
    }
}