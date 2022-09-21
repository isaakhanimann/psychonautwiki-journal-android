package com.isaakhanimann.healthassistant.ui.utils

import java.util.*

fun getTimeDifferenceText(fromDate: Date, toDate: Date): String {
    val diff: Long = toDate.time - fromDate.time
    val seconds = diff / 1000.0
    val minutes = seconds / 60
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
        String.format("%.1f", minutes) + " minutes"
    }
}