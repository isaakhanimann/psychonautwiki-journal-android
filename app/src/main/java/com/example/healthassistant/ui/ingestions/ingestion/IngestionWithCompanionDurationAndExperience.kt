package com.example.healthassistant.ui.ingestions.ingestion

import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.example.healthassistant.data.substances.RoaDuration

data class IngestionWithCompanionDurationAndExperience(
    val ingestionWithCompanion: IngestionWithCompanion,
    val roaDuration: RoaDuration?,
    val experience: Experience?
)
