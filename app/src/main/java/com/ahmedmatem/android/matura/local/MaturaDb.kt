package com.ahmedmatem.android.matura.local

import android.content.Context
import androidx.room.*
import com.ahmedmatem.android.matura.infrastructure.DB_NAME
import com.ahmedmatem.android.matura.local.daos.TestDao
import com.ahmedmatem.android.matura.local.daos.AccountDao
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.network.models.Token

@Database(entities = [Test::class, Token::class], version = 3)
@TypeConverters(Converters::class)
abstract class MaturaDb : RoomDatabase() {

    abstract val testDao: TestDao
    abstract val tokenDao: AccountDao

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