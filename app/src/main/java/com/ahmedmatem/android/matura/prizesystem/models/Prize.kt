package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Keep
@Entity(tableName = "prize_table")
data class Prize(
    @PrimaryKey val holder: String,
    val periodicallyDue: Int,
    val earned: Int
)

/**
 * Set one-to-one relationships between Prize nad Period tables
 */
@Keep
data class PrizeAndPeriod(
    @Embedded val prize: Prize,
    @Relation(
        parentColumn = "holder",
        entityColumn = "prizeHolder"
    )
    val period: Period
)