package com.isaakhanimann.healthassistant.data.room.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class SubstanceCompanion(
    @PrimaryKey(autoGenerate = false)
    val substanceName: String,
    var color: AdaptiveColor,
)
