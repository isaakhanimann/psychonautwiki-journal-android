package com.isaakhanimann.journal.ui.settings

import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import kotlinx.serialization.Serializable

@Serializable
data class JournalExport(
    val ingestions: List<Ingestion>,
    val experiences: List<Experience>,
    val substanceCompanions: List<SubstanceCompanion>,
    val customSubstances: List<CustomSubstance>
)