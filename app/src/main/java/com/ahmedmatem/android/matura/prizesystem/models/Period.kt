package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmedmatem.android.matura.infrastructure.add
import com.ahmedmatem.android.matura.prizesystem.PrizeConfig
import java.util.*

@Keep
@Entity(tableName = "period_table")
data class Period(
    @PrimaryKey val coinHolder: String,
    var start: Date = Date(),
    var end: Date = Date().add(PrizeConfig.COIN_DEFAULT_PERIOD_DURATION_IN_DAYS),
    val duration: Int = PrizeConfig.COIN_DEFAULT_PERIOD_DURATION_IN_DAYS,
    val cyclic: Boolean = true,
    val measure: DurationMeasure = DurationMeasure.DAYS
)

enum class DurationMeasure(val duration: Int) {
    MILLISECONDS(24 * 60 * 60 * 1000),
    SECONDS(24 * 60 * 60),
    MINUTES(24 * 60),
    HOURS(24),
    DAYS(1),
    WEEKS(7)
}

fun Period.expired() = Calendar.getInstance().time.after(end)

/**
 * Use this extension function to reset period bounds.
 * New period starts from now and end after period duration days.
 */
fun Period.reset() {
    start = Date() // starts now
    // calculate end date
    end = start.add(duration)
}