package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmedmatem.android.matura.prizesystem.PrizeConfig
import com.ahmedmatem.android.matura.prizesystem.contract.IPrize
import com.ahmedmatem.android.matura.prizesystem.exceptions.InsufficientCoinException

@Keep
@Entity(tableName = "coin_table")
data class Coin(
    // username
    @PrimaryKey override val holder: String,
    // coins given by default as a gift from app for specific period
    override var gift: Int = PrizeConfig.COIN_DEFAULT_GIFT_PER_WEEK,
    // coins earned by the user offered in different app activities
    override var earned: Int = 0,
    override var drawableResId: Int,
    // indicator for synchronization status
    override var synced: Boolean = false
) : IPrize {

    @Transient
    override val total: Int = gift + earned

    /**
     * Reduce count number of coin trying first to use gift coin and then earned.
     * @throws InsufficientCoinException if no enough gift and earned coin to bet.
     */

    @Throws(InsufficientCoinException::class)
    override fun bet(amount: Int) {
        if (total >= amount) {
            // first try to use coin from gift
            gift -= amount
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

    override fun add(amount: Int) {
        earned += amount
    }

    override fun reset() {
        gift = PrizeConfig.COIN_DEFAULT_GIFT_PER_WEEK
    }
}
