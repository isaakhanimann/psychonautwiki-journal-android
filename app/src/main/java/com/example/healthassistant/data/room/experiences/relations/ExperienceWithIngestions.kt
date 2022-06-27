package com.example.healthassistant.data.room.experiences.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import java.util.*

data class ExperienceWithIngestions(
    @Embedded val experience: Experience,
    @Relation(
        parentColumn = "id",
        entityColumn = "experienceId"
    ) val ingestions: List<Ingestion>
) {
    val sortDate: Date get() = ingestions.firstOrNull()?.time ?: experience.creationDate
}
