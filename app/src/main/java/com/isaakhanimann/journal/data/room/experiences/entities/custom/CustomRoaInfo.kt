package com.isaakhanimann.journal.data.room.experiences.entities.custom

import com.isaakhanimann.journal.data.substances.AdministrationRoute
import kotlinx.serialization.Serializable

@Serializable
data class CustomRoaInfo(
    val administrationRoute: AdministrationRoute,
    val doseInfo: CustomDoseInfo? = null,
    val durationInfo: CustomDurationInfo? = null
)