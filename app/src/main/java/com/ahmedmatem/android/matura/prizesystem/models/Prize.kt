package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import com.ahmedmatem.android.matura.prizesystem.contract.IPrizeItem


/**
 * Set one-to-one relationships between two tables
 */
@Keep
data class Prize<T: IPrizeItem>(
    @Embedded val prizeItem: T,
    @Relation(
        parentColumn = "holder",
        entityColumn = "prizeHolder"
    )
    val period: Period
)