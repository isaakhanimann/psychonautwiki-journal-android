package com.isaakhanimann.healthassistant.data.room.experiences.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Experience
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import java.time.Instant

data class ExperienceWithIngestionsAndCompanions(
    @Embedded val experience: Experience,
    @Relation(
        entity = Ingestion::class,
        parentColumn = "id",
        entityColumn = "experienceId"
    ) val ingestionsWithCompanions: List<IngestionWithCompanion>
) {
    val sortInstant: Instant get() = ingestionsWithCompanions.firstOrNull()?.ingestion?.time ?: experience.creationDate
}
