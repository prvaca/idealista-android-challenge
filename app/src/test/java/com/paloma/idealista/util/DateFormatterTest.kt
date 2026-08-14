package com.paloma.idealista.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.Locale

class DateFormatterTest {

    @Test
    fun `formatFavoritedDate returns expected format`() {
        val calendar = Calendar.getInstance(AppConstants.SPANISH_LOCALE).apply {
            set(2026, Calendar.AUGUST, 14, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val result = DateFormatter.formatFavoritedDate(calendar.timeInMillis)

        assertEquals("14 ago 2026", result)
    }

    @Test
    fun `formatFavoritedDate handles different months correctly`() {
        val calendar = Calendar.getInstance(AppConstants.SPANISH_LOCALE).apply {
            set(2026, Calendar.JANUARY, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val result = DateFormatter.formatFavoritedDate(calendar.timeInMillis)

        assertEquals("1 ene 2026", result)
    }
}