package com.ahmedmatem.android.matura.prizesystem.models

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import com.ahmedmatem.android.matura.prizesystem.contract.IPrize
import java.util.*


/**
 * Set one-to-one relationships between two tables
 */
@Keep
data class Prize(
    @Embedded val prize: IPrize,
    @Relation(
        parentColumn = "holder",
        entityColumn = "prizeHolder"
    )
    val period: Period
)