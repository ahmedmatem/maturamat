package com.ahmedmatem.android.matura.local

import android.content.Context
import androidx.room.*
import com.ahmedmatem.android.matura.infrastructure.DB_NAME
import com.ahmedmatem.android.matura.local.daos.TestDao
import com.ahmedmatem.android.matura.local.daos.AccountDao
import com.ahmedmatem.android.matura.local.daos.PrizeDao
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.network.models.Token
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.prizesystem.models.Period

@Database(
    entities = [Test::class, Token::class, Coin::class, Period::class],
    version = 1

)
@TypeConverters(Converters::class)
abstract class MaturaDb : RoomDatabase() {

    abstract val testDao: TestDao
    abstract val tokenDao: AccountDao
    abstract val prizeDao: PrizeDao

    companion object {
        private var INSTANCE: MaturaDb? = null

        fun getInstance(context: Context): MaturaDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        MaturaDb::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration() // TODO: remove this line for versioning
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}