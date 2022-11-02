package com.isaakhanimann.healthassistant.ui.settings

import com.isaakhanimann.healthassistant.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Experience
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import kotlinx.serialization.Serializable

@Serializable
data class JournalExport(
    val ingestions: List<Ingestion>,
    val experiences: List<Experience>,
    val substanceCompanions: List<SubstanceCompanion>,
    val customSubstances: List<CustomSubstance>
)