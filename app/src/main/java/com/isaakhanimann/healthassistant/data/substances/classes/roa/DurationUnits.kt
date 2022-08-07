package com.isaakhanimann.healthassistant.data.substances.classes.roa

enum class DurationUnits(val text: String) {
    SECONDS("seconds") {
        override val inSecondsMultiplier: Int
            get() = 1
    },
    MINUTES("minutes") {
        override val inSecondsMultiplier: Int
            get() = 60
    },
    HOURS("hours") {
        override val inSecondsMultiplier: Int
            get() = 3600
    },
    DAYS("days") {
        override val inSecondsMultiplier: Int
            get() = 86400
    };

    abstract val inSecondsMultiplier: Int
}