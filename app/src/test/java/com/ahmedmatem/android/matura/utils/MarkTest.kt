package com.ahmedmatem.android.matura.utils

import android.util.Log
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MarkTest {

    private val percentScale = defaultPercentScale // = listOf(10, 20, 25, 40, 50, 65, 80, 90, 100)
    // maximum delta between expected and actual doubles for which both are still considered equal
    private val delta = 0.009

    @Test
    fun percentEntry_0_poorMark_isCorrect() {
        var mark = Mark.fromPercent(0)
        assertEquals(2.0, mark.value, delta)
        assertEquals(mark.toString(), "Слаб 2.0")
    }

    @Test
    fun percentEntry_TWO_poorMark_isCorrect() {
        // default entry is 10%
        var mark = Mark.fromPercent(percentScale[0])
        assertEquals(2.0, mark.value, delta)
        assertEquals(mark.toString(), "Слаб 2.0")
    }

    @Test
    fun percentEntry_TWO_AND_A_HALF_poorMark_isCorrect() {
        // default entry is 20%
        var mark = Mark.fromPercent(percentScale[1])
        assertEquals(2.5, mark.value, delta)
        assertEquals(mark.toString(), "Слаб 2.5")
    }

    @Test
    fun percentEntry_THREE_badMark_isCorrect() {
        // default entry is 25%
        var mark = Mark.fromPercent(percentScale[2])
        assertEquals(3.0, mark.value, delta)
        assertEquals(mark.toString(), "Среден 3.0")
    }

    @Test
    fun percentEntry_THREE_AND_A_HALF_satisfactoryMark_isCorrect() {
        // default entry is 40%
        var mark = Mark.fromPercent(percentScale[3])
        assertEquals(3.5, mark.value, delta)
        assertEquals(mark.toString(), "Добър 3.5")
    }

    @Test
    fun percentEntry_FOUR_satisfactoryMark_isCorrect() {
        // default entry is 50%
        var mark = Mark.fromPercent(percentScale[4])
        assertEquals(4.0, mark.value, delta)
        assertEquals(mark.toString(), "Добър 4.0")
    }

    @Test
    fun percentEntry_FOUR_AND_A_HALF_satisfactoryMark_isCorrect() {
        // default entry is 65%
        var mark = Mark.fromPercent(percentScale[5])
        assertEquals(4.5, mark.value, delta)
        assertEquals(mark.toString(), "Мн. Добър 4.5")
    }

    @Test
    fun percentEntry_FIVE_goodMark_isCorrect() {
        // default entry is 80%
        var mark = Mark.fromPercent(percentScale[6])
        assertEquals(5.0, mark.value, delta)
        assertEquals(mark.toString(), "Мн. Добър 5.0")
    }

    @Test
    fun percentEntry_FIVE_AND_A_HALF_excellentMark_isCorrect() {
        // default entry is 90%
        var mark = Mark.fromPercent(percentScale[7])
        assertEquals(5.5, mark.value, delta)
        assertEquals(mark.toString(), "Отличен 5.5")
    }

    @Test
    fun percentEntry_SIX_excellentMark_isCorrect() {
        // default entry is 100%
        var mark = Mark.fromPercent(percentScale[8])
        assertEquals(6.0, mark.value, delta)
        assertEquals(mark.toString(), "Отличен 6.0")
    }

    @Test
    fun lessPercent_lessMark() {
        for (p in 0..99) {
            val mark = Mark.fromPercent(p)
            val nextMark = Mark.fromPercent(p + 1)
            assertTrue(mark.value <= nextMark.value)
        }
    }
}