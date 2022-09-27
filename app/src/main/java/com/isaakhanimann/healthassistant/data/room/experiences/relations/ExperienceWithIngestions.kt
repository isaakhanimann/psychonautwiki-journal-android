package com.isaakhanimann.healthassistant.data.room.experiences.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Experience
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import java.time.Instant

data class ExperienceWithIngestions(
    @Embedded val experience: Experience,
    @Relation(
        entity = Ingestion::class,
        parentColumn = "id",
        entityColumn = "experienceId"
    ) val ingestions: List<Ingestion>
) {
    val sortInstant: Instant get() = ingestions.firstOrNull()?.time ?: experience.creationDate
}