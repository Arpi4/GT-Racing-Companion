package com.arpitoth.gtracingcompanion.data

/**
 * Basic input hardening utilities for local persistence.
 * Removes control chars, trims, collapses whitespace and bounds length.
 */
object InputSecurity {

    fun cleanText(raw: String, maxLength: Int): String {
        val noControlChars = raw
            .filterNot { it.isISOControl() && it != '\n' && it != '\t' }
        val singleSpaced = noControlChars.replace(Regex("\\s+"), " ").trim()
        return if (singleSpaced.length <= maxLength) {
            singleSpaced
        } else {
            singleSpaced.take(maxLength)
        }
    }

    fun cleanName(raw: String): String = cleanText(raw, maxLength = 64)

    fun cleanLocation(raw: String): String = cleanText(raw, maxLength = 64)

    fun cleanTitle(raw: String): String = cleanText(raw, maxLength = 96)

    fun cleanTrainingType(raw: String): String = cleanText(raw, maxLength = 40)

    fun clampAge(value: Int): Int = value.coerceIn(12, 80)

    fun clampDurationMinutes(value: Int): Int = value.coerceIn(5, 480)

    fun clampRating(value: Int): Int = value.coerceIn(0, 10)
}
