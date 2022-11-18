/*
 * Copyright (c) 2022. Free Software Foundation, Inc.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

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