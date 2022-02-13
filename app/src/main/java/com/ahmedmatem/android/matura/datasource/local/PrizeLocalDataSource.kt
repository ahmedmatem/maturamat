package com.ahmedmatem.android.matura.datasource.local

import androidx.lifecycle.LiveData
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.daos.IPrizeDao
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.network.Result
import com.ahmedmatem.android.matura.prizesystem.contract.IPrizeItem
import com.ahmedmatem.android.matura.prizesystem.models.Period
import com.ahmedmatem.android.matura.prizesystem.models.Prize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.lang.ClassCastException

class PrizeLocalDataSource<T : IPrizeItem>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val prizeDao: IPrizeDao<T> by lazy {
        val db: MaturaDb by inject(MaturaDb::class.java)
        db.prizeDao as IPrizeDao<T>
    }

    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)
    private val username by lazy { _userPrefs.getUser()?.username }
    private val uuid by lazy { _userPrefs.getUuid() }
    private val usernameOrUuid by lazy { username ?: uuid }

    fun getPrizeItem(): LiveData<T>? {
        return prizeDao.getPrizeItem(usernameOrUuid)
    }

    suspend fun getPrize(): Prize<T>? {
        return withContext(dispatcher) {
            prizeDao.getPrize(usernameOrUuid)
        }
    }

    suspend fun sync(prize: Prize<T>, synced: Boolean) = update(prize, synced)

    suspend fun update(prize: Prize<T>, synced: Boolean = false) {
        prize.prizeItem.synced = synced
        upsert(prize.prizeItem, prize.period)
    }

    private suspend fun upsert(prizeItem: T, prizePeriod: Period) {
        prizeDao.insertPrize(prizeItem)
        prizeDao.insertPeriod(prizePeriod)
    }
}