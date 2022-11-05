package com.isaakhanimann.journal.ui.utils

import java.time.Duration
import java.time.Instant

fun getTimeDifferenceText(fromInstant: Instant, toInstant: Instant): String {
    val diff = Duration.between(fromInstant, toInstant)
    val minutes = diff.toMinutes().toFloat()
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7
    val months = weeks / 4
    val years = months / 12
    return if (years > 1) {
        formatFloatToAtMostOneDecimal(years) + " years"
    } else if (months > 1) {
        formatFloatToAtMostOneDecimal(months) + " months"
    } else if (weeks > 1) {
        formatFloatToAtMostOneDecimal(weeks) + " weeks"
    } else if (days > 1) {
        formatFloatToAtMostOneDecimal(days) + " days"
    } else if (hours > 1) {
        formatFloatToAtMostOneDecimal(hours) + " hours"
    } else {
        formatFloatToAtMostOneDecimal(minutes) + " minutes"
    }
}

private fun formatFloatToAtMostOneDecimal(value: Float): String {
    return String.format("%.1f", value).removeSuffix(".0")
}