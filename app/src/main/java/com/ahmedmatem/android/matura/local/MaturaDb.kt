package com.ahmedmatem.android.matura.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmedmatem.android.matura.infrastructure.DB_NAME
import com.ahmedmatem.android.matura.local.daos.TestDao
import com.ahmedmatem.android.matura.local.entities.TestEntity

@Database(entities = [TestEntity::class], version = 1)
abstract class MaturaDb : RoomDatabase() {

    abstract val testDao: TestDao

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
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}