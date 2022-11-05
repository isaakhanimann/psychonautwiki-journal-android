package com.isaakhanimann.journal.data.room.experiences.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion

data class IngestionWithExperience(
    @Embedded
    var ingestion: Ingestion,

    @Relation(
        parentColumn = "experienceId",
        entityColumn = "id"
    )
    var experience: Experience
)