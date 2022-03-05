package com.ahmedmatem.android.matura.datasource.local

import android.util.Log
import com.ahmedmatem.android.matura.local.MaturaDb
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import org.koin.java.KoinJavaComponent.inject

class CoinLocalDataSource {

    private val coinDao by lazy {
        val db: MaturaDb by inject(MaturaDb::class.java)
        db.coinDao
    }

    private val _userPrefs: UserPrefs by inject(UserPrefs::class.java)

    suspend fun getCoin(): Coin? {
        val user = _userPrefs.getUser()
        return user?.let {
            Log.d("DEBUG", "getCoin: username")
            coinDao.getCoin(it.username)
        }
    }

    suspend fun sync(coin: Coin) = insert(coin)

    // Insert prize - OnConflictStrategy is REPLACE
    suspend fun insert(coin: Coin) {
        coinDao.insertCoin(coin)
    }
}