package com.isaakhanimann.healthassistant.ui.utils

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
        String.format("%.1f", years) + " years"
    } else if (months > 1) {
        String.format("%.1f", months) + " months"
    } else if (weeks > 1) {
        String.format("%.1f", weeks) + " weeks"
    } else if (days > 1) {
        String.format("%.1f", days) + " days"
    } else if (hours > 1) {
        String.format("%.1f", hours) + " hours"
    } else {
        "$minutes minutes"
    }
}