package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation

/**
 * Set one-to-one relationships between two tables
 */
@Keep
data class CoinPrize(
    @Embedded val coin: Coin,
    @Relation(
        parentColumn = "holder",
        entityColumn = "prizeHolder"
    )
    val period: Period
)