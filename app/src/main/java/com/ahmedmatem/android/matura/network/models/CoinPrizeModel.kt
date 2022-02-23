package com.ahmedmatem.android.matura.network.models

import androidx.annotation.Keep
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.infrastructure.add
import com.ahmedmatem.android.matura.prizesystem.PrizeConfig
import com.ahmedmatem.android.matura.prizesystem.PrizeConfig.COIN_DRAWABLE_RES_ID
import com.ahmedmatem.android.matura.prizesystem.models.Coin
import com.ahmedmatem.android.matura.prizesystem.models.CoinPrize
import com.ahmedmatem.android.matura.prizesystem.models.DurationMeasure
import com.ahmedmatem.android.matura.prizesystem.models.Period
import com.ahmedmatem.android.matura.utils.providers.ResourcesProvider
import com.squareup.moshi.Json
import org.koin.java.KoinJavaComponent.inject
import java.util.*

@Keep
data class CoinPrizeModel(
    @Json(name = "Holder") val holder: String,
    @Json(name = "Gift") var gift: Int = PrizeConfig.COIN_DEFAULT_GIFT_PER_WEEK,
    @Json(name = "Earned") var earned: Int = 0,
    @Json(name = "Synced") var synced: Boolean = false,
    @Json(name = "Start") var start: Date = Date(),
    @Json(name = "End") var end: Date = Date().add(PrizeConfig.COIN_DEFAULT_PERIOD_DURATION_IN_DAYS),
    @Json(name = "Duration") val duration: Int = PrizeConfig.COIN_DEFAULT_PERIOD_DURATION_IN_DAYS,
    @Json(name = "Cyclic") val cyclic: Boolean = true,
    @Json(name = "Measure") val measure: DurationMeasure = DurationMeasure.DAYS
)

fun CoinPrizeModel.toCoinPrize(): CoinPrize {
    val coin = Coin(holder, gift, earned, COIN_DRAWABLE_RES_ID, synced)
    val period = Period(holder, start, end, duration, cyclic, measure)
    return CoinPrize(coin, period)
}