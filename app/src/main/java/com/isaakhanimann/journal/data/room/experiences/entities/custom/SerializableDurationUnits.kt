package com.isaakhanimann.journal.data.room.experiences.entities.custom

import kotlinx.serialization.Serializable

@Serializable
enum class SerializableDurationUnits(val text: String) {
    SECONDS("seconds"),
    MINUTES("minutes"),
    HOURS("hours"),
    DAYS("days")
}