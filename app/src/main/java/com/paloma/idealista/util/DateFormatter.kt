package com.paloma.idealista.util

import java.text.SimpleDateFormat
import java.util.Date

object DateFormatter {
    private val formatter = SimpleDateFormat(
        AppConstants.DATE_PATTERN,
        AppConstants.SPANISH_LOCALE
    )

    fun formatFavoritedDate(timestampMillis: Long): String {
        return formatter.format(Date(timestampMillis))
    }
}