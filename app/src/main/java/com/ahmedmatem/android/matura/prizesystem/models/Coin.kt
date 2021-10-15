package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.ahmedmatem.android.matura.prizesystem.PrizeConfig

@Keep
@Entity(tableName = "coin_table")
data class Coin(
    // username
    @PrimaryKey val holder: String,
    // coins given by default as a gift from app for specific period
    val gift: Int = PrizeConfig.DEFAULT_COINS_PER_WEEK,
    // coins earned by the user offered in different app activities
    val earned: Int = 0
)

/**
 * Set one-to-one relationships between two tables
 */
@Keep
data class Prize(
    @Embedded val coin: Coin,
    @Relation(
        parentColumn = "holder",
        entityColumn = "coinHolder"
    )
    val period: Period
)