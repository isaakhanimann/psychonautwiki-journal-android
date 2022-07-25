package com.isaakhanimann.healthassistant.data.room.experiences.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion

data class CompanionWithIngestions(
    @Embedded val substanceCompanion: SubstanceCompanion,
    @Relation(
        parentColumn = "substanceName",
        entityColumn = "substanceName"
    ) val ingestions: List<Ingestion>
)