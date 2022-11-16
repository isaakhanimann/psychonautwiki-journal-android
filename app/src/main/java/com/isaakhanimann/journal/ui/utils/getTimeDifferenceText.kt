/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
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