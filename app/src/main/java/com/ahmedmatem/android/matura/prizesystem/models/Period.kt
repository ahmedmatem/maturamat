package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Keep
@Entity(tableName = "period_table")
data class Period(
    @PrimaryKey val id: Long,
    val start: Date,
    val end: Date,
    val duration: Int,
    val cyclic: Boolean = true,
    val measure: DurationMeasure = DurationMeasure.DAYS
)

enum class DurationMeasure(duration: Int) {
    MILLISECONDS(24 * 60 * 60 * 1000),
    SECONDS(24 * 60 * 60),
    MINUTES(24 * 60),
    HOURS(24),
    DAYS(1),
    WEEKS(7)
}