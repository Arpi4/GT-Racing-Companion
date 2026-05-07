package com.arpitoth.gtracingcompanion

import com.arpitoth.gtracingcompanion.data.InputSecurity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InputSecurityTest {

    @Test
    fun cleanText_removesControlCharactersAndNormalizesSpacing() {
        val raw = "  Max\u0000   Verstappen \n\t"
        val cleaned = InputSecurity.cleanText(raw, maxLength = 64)

        assertEquals("Max Verstappen", cleaned)
        assertFalse(cleaned.contains('\u0000'))
    }

    @Test
    fun cleanText_respectsMaxLength() {
        val cleaned = InputSecurity.cleanText("123456789", maxLength = 5)
        assertEquals("12345", cleaned)
    }

    @Test
    fun clampAge_keepsValueInsideAllowedRange() {
        assertEquals(12, InputSecurity.clampAge(5))
        assertEquals(44, InputSecurity.clampAge(44))
        assertEquals(80, InputSecurity.clampAge(120))
    }

    @Test
    fun clampDurationMinutes_keepsValueInsideAllowedRange() {
        assertEquals(5, InputSecurity.clampDurationMinutes(1))
        assertEquals(90, InputSecurity.clampDurationMinutes(90))
        assertEquals(480, InputSecurity.clampDurationMinutes(600))
    }

    @Test
    fun clampRating_keepsValueInsideAllowedRange() {
        assertEquals(0, InputSecurity.clampRating(-1))
        assertEquals(7, InputSecurity.clampRating(7))
        assertEquals(10, InputSecurity.clampRating(14))
        assertTrue(InputSecurity.clampRating(8) in 0..10)
    }

    @Test
    fun cleanName_trimsAndCollapsesWhitespace() {
        val cleaned = InputSecurity.cleanName("   Lewis     Hamilton   ")
        assertEquals("Lewis Hamilton", cleaned)
    }

    @Test
    fun cleanLocation_removesControlCharacters() {
        val cleaned = InputSecurity.cleanLocation("Monza\u0007 Circuit")
        assertEquals("Monza Circuit", cleaned)
        assertFalse(cleaned.contains('\u0007'))
    }

    @Test
    fun cleanTitle_appliesLengthLimit() {
        val longTitle = "A".repeat(150)
        val cleaned = InputSecurity.cleanTitle(longTitle)
        assertEquals(96, cleaned.length)
    }

    @Test
    fun cleanTrainingType_appliesLengthLimit() {
        val longType = "Szimulátor ".repeat(10)
        val cleaned = InputSecurity.cleanTrainingType(longType)
        assertTrue(cleaned.length <= 40)
    }

    @Test
    fun cleanText_preservesReadableHungarianCharacters() {
        val cleaned = InputSecurity.cleanText("Árpád  Nürburgring", maxLength = 64)
        assertEquals("Árpád Nürburgring", cleaned)
    }
}
