package com.ahmedmatem.android.matura.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ahmedmatem.android.matura.infrastructure.DB_NAME
import com.ahmedmatem.android.matura.local.daos.TestDao
import com.ahmedmatem.android.matura.local.daos.AccountDao
import com.ahmedmatem.android.matura.local.daos.CoinDao
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.network.models.User
import com.ahmedmatem.android.matura.prizesystem.models.Coin

@Database(
    entities = [Test::class, User::class, Coin::class],
    version = 6
)
@TypeConverters(Converters::class)
abstract class MaturaDb : RoomDatabase() {

    abstract val testDao: TestDao
    abstract val accountDao: AccountDao
    abstract val coinDao: CoinDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: MaturaDb? = null

        fun getInstance(context: Context): MaturaDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context): MaturaDb {
            return Room.databaseBuilder(context, MaturaDb::class.java, DB_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                        }
                    }
                )
                .fallbackToDestructiveMigration() // TODO: remove this line for versioning
                .build()
        }
    }
}