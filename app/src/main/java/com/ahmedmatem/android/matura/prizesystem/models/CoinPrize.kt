package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import com.ahmedmatem.android.matura.network.models.CoinPrizeModel

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

fun CoinPrize.reset() {
    coin.reset()
    period.reset()
}

fun CoinPrize.toDomainModel(): CoinPrizeModel {
    return CoinPrizeModel(
        coin.holder,
        coin.gift,
        coin.earned,
        coin.synced,
        period.start,
        period.end,
        period.duration,
        period.cyclic,
        period.measure
    )
}