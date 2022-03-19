package com.ahmedmatem.android.matura.utils

import org.junit.Assert.*
import org.junit.Test

class MarkTest {

    @Test
    fun percentEntryMarks_areCorrect() {
        val delta = 0.009
        // 0%
        var mark = Mark.fromPercent(0)
        assertEquals(2.0, mark.value, delta)

        // 10%
        mark = Mark.fromPercent(10)
        assertEquals(2.0, mark.value, delta)

        // 20%
        mark = Mark.fromPercent(20)
        assertEquals(2.5, mark.value, delta)

        // 25%
        mark = Mark.fromPercent(25)
        assertEquals(3.0, mark.value, delta)

        // 40%
        mark = Mark.fromPercent(40)
        assertEquals(3.5, mark.value, delta)

        // 50%
        mark = Mark.fromPercent(50)
        assertEquals(4.0, mark.value, delta)

        // 65%
        mark = Mark.fromPercent(65)
        assertEquals(4.5, mark.value, delta)

        // 80%
        mark = Mark.fromPercent(80)
        assertEquals(5.0, mark.value, delta)

        // 90%
        mark = Mark.fromPercent(90)
        assertEquals(5.5, mark.value, delta)

        // 100%
        mark = Mark.fromPercent(100)
        assertEquals(6.0, mark.value, delta)
    }

    @Test
    fun lessPercent_lessMark(){
        for (p in 0..99) {
            val mark = Mark.fromPercent(p)
            val nextMark = Mark.fromPercent(p + 1)
            assertTrue(mark.value <= nextMark.value)
        }
    }
}