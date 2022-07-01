package com.example.healthassistant.ui.utils

import java.util.*
import kotlin.math.roundToInt

fun getTimeDifferenceText(fromDate: Date, toDate: Date): String {
    val diff: Long = toDate.time - fromDate.time
    val seconds = diff / 1000.0
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val months = days / 30
    val years = months / 12
    val yearsRounded = years.roundToInt()
    val monthsRounded = months.roundToInt()
    val daysRounded = days.roundToInt()
    val hoursRounded = hours.roundToInt()
    val minutesRounded = minutes.roundToInt()
    return if (yearsRounded == 1) {
        "1 year"
    } else if (yearsRounded != 0) {
        "$yearsRounded years"
    } else if (monthsRounded == 1) {
        "1 month"
    } else if (monthsRounded != 0) {
        "$monthsRounded months"
    } else if (daysRounded == 1) {
        "1 day"
    } else if (daysRounded != 0) {
        "$daysRounded days"
    } else if (hoursRounded == 1) {
        "1 hour"
    } else if (hoursRounded != 0) {
        "$hoursRounded hours"
    } else if (minutesRounded == 1) {
        "1 minute"
    } else {
        "$minutesRounded minutes"
    }
}