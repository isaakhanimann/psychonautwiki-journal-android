package com.isaakhanimann.healthassistant.data.room.experiences.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Experience
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion

data class IngestionWithExperience(
    @Embedded
    var ingestion: Ingestion,

    @Relation(
        parentColumn = "experienceId",
        entityColumn = "id"
    )
    var experience: Experience
)