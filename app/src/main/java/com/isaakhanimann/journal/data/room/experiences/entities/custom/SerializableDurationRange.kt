package com.isaakhanimann.journal.data.room.experiences.entities.custom

import kotlinx.serialization.Serializable

@Serializable
data class SerializableDurationRange(
    val min: Float? = null,
    val max: Float? = null,
    val units: SerializableDurationUnits? = null
)