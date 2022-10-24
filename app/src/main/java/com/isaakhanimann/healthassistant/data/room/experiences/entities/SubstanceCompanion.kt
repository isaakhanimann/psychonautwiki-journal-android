package com.isaakhanimann.healthassistant.data.room.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubstanceCompanion(
    @PrimaryKey(autoGenerate = false)
    val substanceName: String,
    var color: AdaptiveColor,
)
