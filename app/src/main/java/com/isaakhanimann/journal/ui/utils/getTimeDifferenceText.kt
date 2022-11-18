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

package com.isaakhanimann.journal.ui.utils

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

fun getTimeDifferenceText(fromInstant: Instant, toInstant: Instant): String {
    val diff = Duration.between(fromInstant, toInstant)
    val minutes = diff.toMinutes().toFloat()
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7
    val months = weeks / 4
    val years = months / 12
    return if (years > 2) {
        ChronoUnit.YEARS.between(
            fromInstant.getLocalDateTime(),
            toInstant.getLocalDateTime()
        ).toString() + " years"
    } else if (months > 2) {
        ChronoUnit.MONTHS.between(
            fromInstant.getLocalDateTime(),
            toInstant.getLocalDateTime()
        ).toString() + " months"
    } else if (weeks > 2) {
        ChronoUnit.WEEKS.between(
            fromInstant.getLocalDateTime(),
            toInstant.getLocalDateTime()
        ).toString() + " weeks"
    } else if (days > 2) {
        ChronoUnit.DAYS.between(
            fromInstant.getLocalDateTime(),
            toInstant.getLocalDateTime()
        ).toString() + " days"
    } else if (hours > 2) {
        ChronoUnit.HOURS.between(
            fromInstant.getLocalDateTime(),
            toInstant.getLocalDateTime()
        ).toString() + " hours"
    } else {
        ChronoUnit.MINUTES.between(
            fromInstant.getLocalDateTime(),
            toInstant.getLocalDateTime()
        ).toString() + " minutes"
    }
}