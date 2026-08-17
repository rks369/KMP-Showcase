package org.rks369.news.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private val MONTH_NAMES = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)

@OptIn(ExperimentalTime::class)
fun formatPublishedDate(publishedDate: String?): String? {
    if (publishedDate.isNullOrBlank()) return null
    val localDateTime = runCatching {
        Instant.parse(publishedDate).toLocalDateTime(TimeZone.currentSystemDefault())
    }.getOrNull() ?: return publishedDate

    return "${MONTH_NAMES[localDateTime.month.ordinal]} ${localDateTime.day}, ${localDateTime.year} · " +
        localDateTime.formatAsTime()
}

private fun LocalDateTime.formatAsTime(): String {
    val amPm = if (hour >= 12) "PM" else "AM"
    val hour12 = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return "${hour12.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $amPm"
}
