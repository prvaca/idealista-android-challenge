package com.paloma.idealista.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private val formatter = SimpleDateFormat("d MMM yyyy", Locale("es", "ES"))

    fun formatFavoritedDate(timestampMillis: Long): String {
        return formatter.format(Date(timestampMillis))
    }
}