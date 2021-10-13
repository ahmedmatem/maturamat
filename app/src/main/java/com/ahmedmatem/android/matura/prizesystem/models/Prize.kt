package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Keep
@Entity(tableName = "prize_table")
data class Prize(
    @PrimaryKey val id: Long,
    val holder: String, // user@name
    val periodicallyDue: Int,
    val earned: Int,
    val periodId: String
)

/**
 * Set one-to-one relationships between Prize nad Period tables
 */
@Keep
data class PrizeAndPeriod(
    @Embedded val period: Period,
    @Relation(
        parentColumn = "id",
        entityColumn = "periodId"
    )
    val prize: Prize
)