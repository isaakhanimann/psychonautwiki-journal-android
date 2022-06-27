package com.example.healthassistant.data.room.experiences.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import java.util.*

data class ExperienceWithIngestionsAndCompanions(
    @Embedded val experience: Experience,
    @Relation(
        entity = Ingestion::class,
        parentColumn = "id",
        entityColumn = "experienceId"
    ) val ingestionsWithCompanions: List<IngestionWithCompanion>
) {
    val sortDate: Date get() = ingestionsWithCompanions.firstOrNull()?.ingestion?.time ?: experience.creationDate
}
