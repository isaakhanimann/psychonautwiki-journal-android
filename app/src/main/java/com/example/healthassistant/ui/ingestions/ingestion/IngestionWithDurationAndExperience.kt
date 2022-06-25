package com.example.healthassistant.ui.ingestions.ingestion

import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.RoaDuration

data class IngestionWithDurationAndExperience(
    val ingestion: Ingestion,
    val roaDuration: RoaDuration?,
    val experience: Experience?
)
