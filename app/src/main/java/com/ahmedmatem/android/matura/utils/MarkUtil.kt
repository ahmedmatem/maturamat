package com.ahmedmatem.android.matura.utils

import java.lang.IllegalArgumentException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidParameterException

val defaultPercentScale = listOf(10, 20, 25, 40, 50, 65, 80, 90, 100)

val percentScale = defaultPercentScale

enum class PercentEntry(val percent: Int) {
    TWO(percentScale[0]),
    TWO_AND_A_HALF(percentScale[1]),
    THREE(percentScale[2]),
    THREE_AND_A_HALF(percentScale[3]),
    FOUR(percentScale[4]),
    FOUR_AND_A_HALF(percentScale[5]),
    FIVE(percentScale[6]),
    FIVE_AND_A_HALF(percentScale[7]),
    SIX(percentScale[8]);

    @Throws(InvalidAlgorithmParameterException::class)
    fun incrementPercent(): Int {
        return if (percent + 1 < 100)
            percent + 1
        else
            throw InvalidAlgorithmParameterException("Increment leads to out of range exception.")
    }
}

enum class MarkText(val text: String) {
    POOR("Слаб"),
    BAD("Среден"),
    SATISFACTORY("Добър"),
    GOOD("Мн. Добър"),
    EXCELLENT("Отличен")
}

class Mark private constructor(val value: Double, val text: String) {

    private class Interval(
        private val startPercent: Int,
        private val endPercent: Int
    ) {
        private val startMark = percentToMarkMapper[startPercent]
        private val endMark = percentToMarkMapper[endPercent]

        fun markUnit(): Double {
            val markWidth = endMark!! - startMark!!
            return markWidth / length()
        }

        private fun length(): Int {
            val len = endPercent - startPercent
            return if (len == 0) 1 else len
        }

        @Throws(InvalidParameterException::class)
        private fun markTextByValue(value: Double): String {
            return when (value) {
                in 2.0..2.99 -> MarkText.POOR.text
                in 3.0..3.49 -> MarkText.BAD.text
                in 3.5..4.49 -> MarkText.SATISFACTORY.text
                in 4.5..5.49 -> MarkText.GOOD.text
                in 5.5..6.0 -> MarkText.EXCELLENT.text
                else -> throw InvalidParameterException(
                    "Parameter value = $value must be between 2.0 and 6.0"
                )
            }
        }

        fun produceMark(): Mark {
            val percentOffset = percent - startPercent
            val markValue = percentToMarkMapper[startPercent]!! + markUnit() * percentOffset
            return Mark(markValue, markTextByValue(markValue))
        }
    }

    companion object {
        private var percent: Int = 0

        private val percentToMarkMapper = mapOf(
            PercentEntry.TWO.percent to 2.0,
            PercentEntry.TWO_AND_A_HALF.percent to 2.5,
            PercentEntry.THREE.percent to 3.0,
            PercentEntry.THREE_AND_A_HALF.percent to 3.5,
            PercentEntry.FOUR.percent to 4.0,
            PercentEntry.FOUR_AND_A_HALF.percent to 4.5,
            PercentEntry.FIVE.percent to 5.0,
            PercentEntry.FIVE_AND_A_HALF.percent to 5.5,
            PercentEntry.SIX.percent to 6.0
        )

        @Throws(IllegalArgumentException::class, InvalidParameterException::class)
        fun fromPercent(percent: Int): Mark {
            this.percent = percent
            val interval = when (percent) {
                in 0..PercentEntry.TWO.percent ->
                    Interval(PercentEntry.TWO.percent, PercentEntry.TWO.percent)
                in PercentEntry.TWO.incrementPercent()..PercentEntry.TWO_AND_A_HALF.percent ->
                    Interval(PercentEntry.TWO.percent, PercentEntry.TWO_AND_A_HALF.percent)
                in PercentEntry.TWO_AND_A_HALF.incrementPercent()..PercentEntry.THREE.percent ->
                    Interval(PercentEntry.TWO_AND_A_HALF.percent, PercentEntry.THREE.percent)
                in PercentEntry.THREE.incrementPercent()..PercentEntry.THREE_AND_A_HALF.percent ->
                    Interval(PercentEntry.THREE.percent, PercentEntry.THREE_AND_A_HALF.percent)
                in PercentEntry.THREE_AND_A_HALF.incrementPercent()..PercentEntry.FOUR.percent ->
                    Interval(PercentEntry.THREE_AND_A_HALF.percent, PercentEntry.FOUR.percent)
                in PercentEntry.FOUR.incrementPercent()..PercentEntry.FOUR_AND_A_HALF.percent ->
                    Interval(PercentEntry.FOUR.percent, PercentEntry.FOUR_AND_A_HALF.percent)
                in PercentEntry.FOUR_AND_A_HALF.incrementPercent()..PercentEntry.FIVE.percent ->
                    Interval(PercentEntry.FOUR_AND_A_HALF.percent, PercentEntry.FIVE.percent)
                in PercentEntry.FIVE.incrementPercent()..PercentEntry.FIVE_AND_A_HALF.percent ->
                    Interval(PercentEntry.FIVE.percent, PercentEntry.FIVE_AND_A_HALF.percent)
                in PercentEntry.FIVE_AND_A_HALF.incrementPercent()..PercentEntry.SIX.percent ->
                    Interval(PercentEntry.FIVE_AND_A_HALF.percent, PercentEntry.SIX.percent)
                else -> throw IllegalArgumentException("Percent must be integer between 0 and 100.")
            }

            return interval.produceMark()
        }
    }
}