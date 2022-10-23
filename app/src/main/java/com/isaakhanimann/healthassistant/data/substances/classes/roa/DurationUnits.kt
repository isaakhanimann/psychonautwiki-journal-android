package com.isaakhanimann.healthassistant.data.substances.classes.roa

enum class DurationUnits(val text: String) {
    SECONDS("seconds") {
        override val inSecondsMultiplier = 1
        override val shortText = "s"
    },
    MINUTES("minutes") {
        override val inSecondsMultiplier = 60
        override val shortText = "m"
    },
    HOURS("hours") {
        override val inSecondsMultiplier = 3600
        override val shortText = "h"
    },
    DAYS("days") {
        override val inSecondsMultiplier = 86400
        override val shortText = "d"
    };

    abstract val inSecondsMultiplier: Int
    abstract val shortText: String
}