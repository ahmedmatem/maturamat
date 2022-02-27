package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ahmedmatem.android.matura.infrastructure.add
import com.ahmedmatem.android.matura.prizesystem.PrizeConfig
import com.ahmedmatem.android.matura.prizesystem.PrizeConfig.COIN_DRAWABLE_RES_ID
import com.ahmedmatem.android.matura.prizesystem.exceptions.InsufficientCoinException
import com.squareup.moshi.Json
import java.util.*

@Keep
@Entity(tableName = "coin_table")
data class Coin(
    // username
    @PrimaryKey @Json(name = "Holder") val holder: String = "",
    // coins given by default as a gift from app for specific period
    @Json(name = "Gift") var gift: Int = PrizeConfig.COIN_DEFAULT_GIFT_PER_WEEK,
    // coins earned by the user offered in different app activities
    @Json(name = "Earned") var earned: Int = 0,
    @Json(name = "Start") var start: Date = Date(),
    @Json(name = "End") var end: Date = Date().add(PrizeConfig.COIN_DEFAULT_PERIOD_DURATION_IN_DAYS),
    @Json(name = "Duration") var duration: Int = PrizeConfig.COIN_DEFAULT_PERIOD_DURATION_IN_DAYS,
    @Json(name = "Measure") var measure: Int = DurationMeasure.DAYS.ordinal
) {
    @Transient
    @Ignore
    val total: Int = gift + earned

    @Ignore
    @Transient
    val drawableResId: Int = COIN_DRAWABLE_RES_ID

    /**
     * Reduce count number of coin trying first to use gift coin and then earned.
     * @throws InsufficientCoinException if no enough gift and earned coin to bet.
     */

    @Throws(InsufficientCoinException::class)
    fun bet(amount: Int = 1) {
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

    fun add(amount: Int) {
        earned += amount
    }
}

enum class DurationMeasure {
    MILLISECONDS,
    SECONDS,
    MINUTES,
    HOURS,
    DAYS,
    WEEKS
}

// Extensions

fun Coin.expired(): Boolean = Calendar.getInstance().time.after(end)

fun Coin.reset() {
    gift = PrizeConfig.COIN_DEFAULT_GIFT_PER_WEEK
    start = Date()
    end = start.add(duration)
}

fun Coin.asKeyValueMap(): List<Pair<String, Any?>> {
    return listOf(
        "holder" to holder,
        "gift" to gift,
        "earned" to earned,
        "start" to start.time,
        "end" to end.time,
        "duration" to duration,
        "measure" to measure
    )
}

fun Map<String, Any?>.asCoin(): Coin {
    return Coin(
        this["holder"].toString(),
        this["gift"] as Int,
        this["earned"] as Int,
        Date(this["start"] as Long),
        Date(this["end"] as Long),
        this["duration"] as Int,
        this["measure"] as Int
    )
}
