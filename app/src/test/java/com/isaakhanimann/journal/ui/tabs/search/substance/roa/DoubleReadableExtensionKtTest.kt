package com.isaakhanimann.journal.ui.tabs.search.substance.roa

import junit.framework.TestCase.assertEquals
import org.junit.Test

class DoubleReadableExtensionKtTest {

    @Test
    fun testToReadableString() {
        assertEquals("2", 2.0.toReadableString())
        assertEquals("20", 20.0.toReadableString())
        assertEquals("0.33", (1.0/3.0).toReadableString())
        assertEquals("120", 120.0.toReadableString())
        assertEquals("1.5", 1.5.toReadableString())
        assertEquals("1500", 1500.0.toReadableString())
        assertEquals("12.6", 12.6.toReadableString())
        assertEquals("0.25", 0.25.toReadableString())
        assertEquals("3.33", (10.0/3.0).toReadableString())
        assertEquals("333", (1000.0/3.0).toReadableString())
        assertEquals("33.3", (100.0/3.0).toReadableString())
        assertEquals("1.67", 1.66666.toReadableString())
        assertEquals("123", 122.66666.toReadableString())
        assertEquals("122", 122.33333.toReadableString())
        assertEquals("44.4", 44.4444.toReadableString())
    }
}