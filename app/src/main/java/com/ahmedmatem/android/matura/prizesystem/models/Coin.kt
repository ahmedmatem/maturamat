package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmedmatem.android.matura.prizesystem.PrizeConfig
import com.ahmedmatem.android.matura.prizesystem.exceptions.InsufficientCoinException

@Keep
@Entity(tableName = "coin_table")
data class Coin(
    // username
    @PrimaryKey val holder: String,
    // coins given by default as a gift from app for specific period
    var gift: Int = PrizeConfig.DEFAULT_GIFT_PER_WEEK,
    // coins earned by the user offered in different app activities
    var earned: Int = 0,
    // indicator for synchronization status
    var synced: Boolean = false
)

/**
 * Extension functions
 */

fun Coin.resetGift() {
    gift = PrizeConfig.DEFAULT_GIFT_PER_WEEK
}

/**
 * Add 'count' coin as earned coin
 */
fun Coin.add(count: Int = 1) {
    earned += count
}

/**
 * Reduce count number of coin trying first to use gift coin and then earned.
 * @throws InsufficientCoinException if no enough gift and earned coin to bet.
 */
fun Coin.bet(count: Int = 1) {
    val allCoin = gift + earned
    if (allCoin >= count) {
        // first try to use coin from gift
        gift -= count
        // in case of no enough gift coin to bet
        if (gift < 0) {
            // reduce earned coin with rest of the count
            earned += gift
            // and set gift to zero
            gift = 0
        }
    } else {
        throw InsufficientCoinException()
    }
}

fun Coin.total(): Int {
    return gift + earned
}
