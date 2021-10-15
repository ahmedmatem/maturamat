package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import java.util.*


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