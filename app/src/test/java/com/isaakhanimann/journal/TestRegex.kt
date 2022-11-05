package com.isaakhanimann.journal

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TestRegex {
    @Test
    fun testRegex() {
        val regex = Regex(
            pattern = "5-MeO-xxT".replace(oldValue = "x", newValue = "[\\S]*", ignoreCase = true),
            option = RegexOption.IGNORE_CASE
        )
        assertTrue(regex.matches("5-MeO-DALT"))
        assertTrue(regex.matches("5-MeO-DMT"))
        assertTrue(regex.matches("5-MeO-DiPT"))
        assertTrue(regex.matches("5-MeO-EiPT"))
        assertFalse(regex.matches("something else"))
    }
}